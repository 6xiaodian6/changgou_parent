package com.changgou.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.order.config.RabbitMQConfig;
import com.changgou.order.dao.OrderItemMapper;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.dao.TaskMapper;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.pojo.Task;
import com.changgou.order.service.CartService;
import com.changgou.order.service.OrderService;
import com.changgou.util.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CartService cartService;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    private static final String CART = "Cart_";

    /**
     * 查询全部列表
     *
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public Order findById(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    /**
     * 将购物车数据添加到订单中
     * 分布式事务处理，添加fescar进行分布式事务的处理，处理逻辑就是在调用的方法上添加@GlobalTransactional(name = "order_add")注解
     * 然后就会进行事务的传递，从而就可以把不相关联的两个事务连接到一起了，但是它们之间的相互执行是互不影响的，都是本地事务的提交，然后由fescar
     * 进行消息的传递，如果有一个事务执行不成功就会通知所有的事务进行回滚的操作，但这是使用undolog这个表进行sql语句的执行记录的，回滚的时候就是靠着这个
     * 表里面的数据进行反向sql的执行的，以此来完成事务的回滚
     *
     * @param order
     */
//    @GlobalTransactional(name = "order_add")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String add(Order order) {
        //1.获取所有购物项
        Map cartMap = cartService.list(order.getUsername());
        List<OrderItem> orderItemList = (List<OrderItem>) cartMap.get("orderItemList");
        //2.统计计算:总金额,总数量
        //3.填充订单数据并保存到tb_order
        order.setTotalNum((Integer) cartMap.get("totalNum"));
        order.setTotalMoney((Integer) cartMap.get("totalPrice"));
        order.setPayMoney((Integer) cartMap.get("totalPrice"));
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setBuyerRate("0"); // 0:未评价  1:已评价
        order.setSourceType("1"); //1:WEB
        order.setOrderStatus("0"); //0:未完成 1:已完成 2:已退货
        order.setPayStatus("0"); //0:未支付 1:已支付
        order.setConsignStatus("0"); //0:未发货 1:已发货
        String orderId = idWorker.nextId() + "";
        order.setId(orderId);
        int count = orderMapper.insertSelective(order);
        //4.填充订单项数据并保存到tb_order_item
        for (OrderItem orderItem : orderItemList) {
            orderItem.setId(idWorker.nextId() + "");
            //0:未退货 1:已退货
            orderItem.setIsReturn("0");
            orderItem.setOrderId(orderId);
            orderItemMapper.insertSelective(orderItem);
        }
        //扣减库存并增加销量,这里是远程服务调用，处理分布式事务问题就在这里
        skuFeign.decrCount(order.getUsername());

        //分布式异常数据回滚测试
//        int i = 1 / 0;
        //添加任务数据
        System.out.println("向订单数据库中的任务表去添加任务数据");
        Task task = new Task();
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        //通过消息队列通知任务执行
        task.setMqExchange(RabbitMQConfig.EX_BUYING_ADDPOINTUSER);
        task.setMqRoutingkey(RabbitMQConfig.CG_BUYING_ADDPOINT_KEY);

        Map map = new HashMap();
        map.put("username", order.getUsername());
        map.put("orderId", orderId);
        map.put("point", order.getPayMoney());
        task.setRequestBody(JSON.toJSONString(map));
        taskMapper.insertSelective(task);
        //5.删除购物车数据(redis)
        redisTemplate.delete(CART + order.getUsername());
        return orderId;
    }


    /**
     * 修改
     *
     * @param order
     */
    @Override
    public void update(Order order) {
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        orderMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    @Override
    public List<Order> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return orderMapper.selectByExample(example);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Order> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return (Page<Order>) orderMapper.selectAll();
    }

    /**
     * 条件+分页查询
     *
     * @param searchMap 查询条件
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @Override
    public Page<Order> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        return (Page<Order>) orderMapper.selectByExample(example);
    }

    /**
     * 构建查询对象
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 订单id
            if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                criteria.andEqualTo("id", searchMap.get("id"));
            }
            // 支付类型，1、在线支付、0 货到付款
            if (searchMap.get("payType") != null && !"".equals(searchMap.get("payType"))) {
                criteria.andEqualTo("payType", searchMap.get("payType"));
            }
            // 物流名称
            if (searchMap.get("shippingName") != null && !"".equals(searchMap.get("shippingName"))) {
                criteria.andLike("shippingName", "%" + searchMap.get("shippingName") + "%");
            }
            // 物流单号
            if (searchMap.get("shippingCode") != null && !"".equals(searchMap.get("shippingCode"))) {
                criteria.andLike("shippingCode", "%" + searchMap.get("shippingCode") + "%");
            }
            // 用户名称
            if (searchMap.get("username") != null && !"".equals(searchMap.get("username"))) {
                criteria.andLike("username", "%" + searchMap.get("username") + "%");
            }
            // 买家留言
            if (searchMap.get("buyerMessage") != null && !"".equals(searchMap.get("buyerMessage"))) {
                criteria.andLike("buyerMessage", "%" + searchMap.get("buyerMessage") + "%");
            }
            // 是否评价
            if (searchMap.get("buyerRate") != null && !"".equals(searchMap.get("buyerRate"))) {
                criteria.andLike("buyerRate", "%" + searchMap.get("buyerRate") + "%");
            }
            // 收货人
            if (searchMap.get("receiverContact") != null && !"".equals(searchMap.get("receiverContact"))) {
                criteria.andLike("receiverContact", "%" + searchMap.get("receiverContact") + "%");
            }
            // 收货人手机
            if (searchMap.get("receiverMobile") != null && !"".equals(searchMap.get("receiverMobile"))) {
                criteria.andLike("receiverMobile", "%" + searchMap.get("receiverMobile") + "%");
            }
            // 收货人地址
            if (searchMap.get("receiverAddress") != null && !"".equals(searchMap.get("receiverAddress"))) {
                criteria.andLike("receiverAddress", "%" + searchMap.get("receiverAddress") + "%");
            }
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if (searchMap.get("sourceType") != null && !"".equals(searchMap.get("sourceType"))) {
                criteria.andEqualTo("sourceType", searchMap.get("sourceType"));
            }
            // 交易流水号
            if (searchMap.get("transactionId") != null && !"".equals(searchMap.get("transactionId"))) {
                criteria.andLike("transactionId", "%" + searchMap.get("transactionId") + "%");
            }
            // 订单状态
            if (searchMap.get("orderStatus") != null && !"".equals(searchMap.get("orderStatus"))) {
                criteria.andEqualTo("orderStatus", searchMap.get("orderStatus"));
            }
            // 支付状态
            if (searchMap.get("payStatus") != null && !"".equals(searchMap.get("payStatus"))) {
                criteria.andEqualTo("payStatus", searchMap.get("payStatus"));
            }
            // 发货状态
            if (searchMap.get("consignStatus") != null && !"".equals(searchMap.get("consignStatus"))) {
                criteria.andEqualTo("consignStatus", searchMap.get("consignStatus"));
            }
            // 是否删除
            if (searchMap.get("isDelete") != null && !"".equals(searchMap.get("isDelete"))) {
                criteria.andEqualTo("isDelete", searchMap.get("isDelete"));
            }

            // 数量合计
            if (searchMap.get("totalNum") != null) {
                criteria.andEqualTo("totalNum", searchMap.get("totalNum"));
            }
            // 金额合计
            if (searchMap.get("totalMoney") != null) {
                criteria.andEqualTo("totalMoney", searchMap.get("totalMoney"));
            }
            // 优惠金额
            if (searchMap.get("preMoney") != null) {
                criteria.andEqualTo("preMoney", searchMap.get("preMoney"));
            }
            // 邮费
            if (searchMap.get("postFee") != null) {
                criteria.andEqualTo("postFee", searchMap.get("postFee"));
            }
            // 实付金额
            if (searchMap.get("payMoney") != null) {
                criteria.andEqualTo("payMoney", searchMap.get("payMoney"));
            }

        }
        return example;
    }

}
