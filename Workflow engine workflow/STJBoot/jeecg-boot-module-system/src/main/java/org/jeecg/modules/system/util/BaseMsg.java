package org.jeecg.modules.system.util;

/**
 *  应该用枚举定义
 * Created by Yang on 2017/2/13.
 */
public class BaseMsg {

    public static final int CODE_SUCCESS = 0;//请求/操作成功code
    public static final int CODE_FAILED = 1;//请求/操作失败code
    public static final int CODE_EXCEPTION = -1;//请求/操作异常code


    /**
     * 解析table
     */
    public static final String PARSE_FAIL = "解析失败";
    /**
     *
     */
    public static final String TASKTIMEOUT = "任务已超时！";
    /**
     *元数据入库使用
     */
    public static final int CODE_DATAINPUT_FAILED = 2;//数据入元数据库失败
    public static final String MSG_DATAINPUT_FAILED = "数据入元数据库失败";
    /**
     *任务提交一次性记录给统计模块失败";
     */
    public static final int CODE_STATIC_OPERATION_FAILED= 3;//任务提交一次性记录给统计模块失败
    public static final String MSG_STATIC_OPERATION_FAILED= "任务记录给统计模块失败";

    /**
     *没有通过培训
     */
    public static final int CODE_NO_TRAIN= 1;
    public static final String MSG_NO_TRAIN= "没有通过培训，赶快去培训吧。。。";
    /**
     * 反馈的提示
     */
    public static final String MSG_REDIS_CONNECT_EXCEPTION = "系统异常";
    public static final String MSG_NOT_CLEAN_WAIT_TASK_LIST = "抱歉，请先处理完待确认的任务再领取新任务";
    public static final String MSG_NO_BORD = "公司员工未设置板块信息，请联系管理员设置";
    public static final String MSG_NOT_LOGIN = "抱歉，未登录";
    public static final String MSG_LACK_FILE_ERROR = "发布失败，缺少文件";
    public static final String MSG_Sql_Exception = "数据库操作异常";
    public static final String MSG_Transformation_Exception = "数据异常，正确率请输入数字！";
    public static final String MSG_Illegal_Operation = "违规操作！";
    public static final String MSG_JSON_FORMAT_ERROR = "json 格式有误";
    public static final String MSG_UPDATE_FORMAT_ERROR = "该任务已被修改并发布";
    public static final String MSG_SUCCESS = "操作成功！";
    public static final String MSG_FAILED = "操作失败！";
    public static final String TASK_CONFINDED = "任务已被确认！";
    public static final String TASK_DEALED = "任务已被处理！";
    public static final String MSG_LOGIN_FAILED = "用户名或密码错误！";
    public static final String MSG_NO_DATA = "无数据！";
    public static final String MSG_EXCEPTION = "操作异常！";
    public static final String MSG_CLAIM_TASK_FAILED="认领失败";
    public static final String MSG_CLAIMED="抱歉，该任务已被别人认领";
    public static final String MSG_CREATE_SUSSESS ="创建成功";
    public static final String MSG_CREATE_FAILED ="创建失败";
    public static final String MSG_DATA_ERROR = "数据错误";
    public static final String MSG_REJECT = "审核不通过";
    public static final String NO_THIS_TASK = "没有这个任务";
    public static final String TEMPORARY_STORAGE_TASK = "该任务数据已暂存";
    public static final String NO_BOARD = "抱歉您还没有所属板块！";
    public static final String NO_TASK_CONFIG = "抱歉，配置任务不存在";
    public static final String NOT_ONLY_TASK_CONFIG_ID = "抱歉，存在重复配置任务ID";
    public static final String EMPTY_REFERENCE_FILE_NAME = "抱歉，该配置任务上次使用的筛选依据文件异常";
    public static final String ERROR_RANK_PARAMETER = "绩效排名参数错误，不得小于1";
    public static final String ERROR_REQUIRE_COMPLETED_TASK_NUM_PARAMETER = "要求的完成个数参数错误，不得小于0";
    public static final String ERROR_BOTH_BULL_OR_ZERO = "完成个数和正确率不得都为空或<=0";
    public static final String ERROR_REQUIRE_RIGHT_RATE = "要求的正确率参数错误，不得小于0";
    public static final String ERROR_EMPTY_REQUIRE_TASK_CONFIG_ID = "未选择指定的配置任务";
    public static final String ERROR_TASK_FLOW_USER_NUM = "抱歉，当前所选人员个数与该配置任务对应的任务流要求不符";

    public static final String MSG_TASK_COUNT_NO_ENOUGH_THREE = "抱歉，当前任务不足三个";
    public static final String MSG_NO_QUESTION = "确认无误！";
    public static final int JSON_ERROR = 2;
    public static final int UPDATE_ERROR = 3;
    public static final String PARAMETER_ERROR_NO_RESULT = "参数有问题或查询无结果";
    public static final String DATE_FORMAT_ERROR = "时间格式有误";
    public static final String TOKEN_ERROR = "token有误";
    public static final String RETURN_VALID_WITH_INVALID_REASON = "原因属实时，不得传退回无效原因";
    public static final String RETURN_INVALID_WITH_VALID_SITUATION = "原因属实时，不得传退回无效原因";
    public static final String PENDING = "请求中";
    public static final String AFFIEMED = "已被确认";
    public static final String DONE = "已被处理";
    public static final String REJECT = "请仔细审查,找出错误";
    public static final String OVER_OF_SUBMIT_TIME = "对不起,你已超过提交次数上限";
    public static final String NO_DELAY_TIME = "任务截止时间不符合发布条件，最少比当前时间不低于半个小时哦！";
    public static final String NO_TIMING_PUBLISH_TIME = "对不起，请输入定时发布时间";
    public static final String ERROR_TIMING_PUBLISH_TIME = "定时发布时间不符合条件，定时发布时间不能比当前时间早哦";
    public static final String ERROR_TIMING_PUBLISH_TIME2 = "定时发布时间或截止时间不符合条件，截止时间至少比定时发布时间晚半个小时哦！";
    public static final String PLZ_ABLE_STATUS = "请先启用对应的配置任务，再重发";
    public static final String TASK_INFORMED = "任务已校验";
    public static final String OVER_FILE_NUM = "抱歉，上传文件个数超出配置！";
    public static final String OVER_URL_NUM = "抱歉，URL个数超出配置！";
    public static final String OVER_URL_LIST_LENGTH = "抱歉，该URL地址不存在！";
    public static final String OVER_FILE_LIST_LENGTH = "抱歉，该文件不存在！";
    /**
     * 从打用户名称和code
     */
    public static final int SUB_USER_CODE = 3;
    public static final String SUB_USER = "从打";
    /**
     * 主打用户名称和code
     */
    public static final int PRIMARY_USER_CODE = 2;
    public static final String PRIMARY_USER = "主打";
    //主从审用户常量
    public static final int PRIMARY_CHECK_USER_CODE = 0;
    public static final String PRIMARY_CHECK_USER = "主审";

    public static final int SUB_CHECK_USER_CODE = 1;
    public static final String SUB_CHECK_USER = "从审";
    //单打审核用户常量
    public static final String CHECK_USER = "审核";
    public static final String SINGLE_USER = "单打";


    //任务批量导入的部分
    public static final String FILE_IS_EMPTY = "没有选择文件";
    public static final String NOT_PERMISSION = "没有这个任务，或者这个任务状态不允许删除";

    //任务申请失败
    public static final String CLAIM_FAILED = "抱歉，申请任务失败！您当前任务没有做完或者您的申请列表还有任务待领取";
    public static final String CLAIM_FAILED_OUTDATE = "抱歉，该任务已过期";
    public static final String CLAIM_FAILED_TIMEOUT= "抱歉，该任务因您长期未操作需重新申请";
    public static final String UMSUOOPRT_MEDIA = "抱歉，此文件不支持此操作";
    public static final String WORKLOADDAY = "抱歉，申请任务失败！您当天的工作量已经达到最大值";
    public static final String WORKLOADMONTH = "抱歉，申请任务失败！您当月的工作量已经达到最大值";





    /**
     * 用户认领任务时角色所对应的权限等级
     * 依次为：主审审核，从审、主打单打、从打 和 没有权限时的权限的默认值
     */
    public static final Integer PRIMARY_CHECK_RANK = 70;
    public static final Integer SUB_CHECK_RANK= 60;
    public static final Integer PRIMARY_USER_RANK = 50;
    public static final Integer SUB_USER_RANK= 40;

    public static final Integer DEFAULT_RANK= 30;


    //任务一次申请的最大个数
    public static final Integer BASE_REQUEST_COUNT= 3;

    /**
     * 任务分支code
     */
    public static final Integer PRIMARY_TASK_CODE = 2;
    public static final Integer SUB_TASK_CODE = 3;
    public static final Integer PRIMARY_CHECK_TASK_CODE= 4;
    public static final Integer SUB_CHECK_TASK_CODE = 5;
    public static final Integer SINGLE_TASK_CODE = 6;
    public static final Integer CHECK_TASK_CODE =  7;
    public static final Integer BRANCH_DEFAULT_CODE =  0;

    /**
     * 申请任务检验用户的信息是否完善使用
     */
    public static final int CODE_CHECK_USER_FAILED = 2;//数据入元数据库失败
    public static final String MSG_CHECK_USER_FAILED = "抱歉，需要您绑定银行卡信息和完善住址信息！";
    public static final String MSG_CHECK_USER_EXCEPTION = "调用User服务获取银行卡信息和完善住址信息失败";

    /**
     * 统计任务工作量模块（外部接口）调用状态通用代码
     */
    public static final int CODE_TASK_SUBMIT= 5;

    /**
     * 任务发送给日志模块信息
     */
    public static final String USER_AFFIRM = "用户确认任务";

    /**
     * 配置任务停用
     */
    public static final int CODE_CANOT_DEL= 1;
    public static final String MSG_CANOT_DEL= "该配置任务目前正在被使用，不符合停用条件，请稍后再试";
    /**
     * 配置任务培训正在使用，不允许删除！
     */
    public static final int CODE_TRAIN_TASKCONF= 1;
    public static final String MSG_TRAIN_TASKCONF_NOSTOP= "配置任务对应的培训课程正在使用，不允许停用！";
    public static final String MSG_TRAIN_TASKCONF= "配置任务对应的培训课程正在使用，不允许删除！";
    public static final String MSG_TRAIN_TASKCONF_EXCEPTION= "查询配置任务是否被培训模块使用出现错误！";
    public static final String MSG_ERROR_STATUS_TO_DELETE_CONFIG = "抱歉，该配置任务目前状态下不可删除";

    public static final int CODE_TASK_EMPTY = 1;
    public static final String MSG_TASK_EMPTY = "抱歉，当前没有可供申请的任务";
    public static final String MSG_TASK_ERROR = "抱歉，您的账户由于超出系统限制已被禁用";
    public static final String MSG_TASK_RIGHT_ERROR = "正确率计算异常";//调取统计接口计算正确率异常

    /**
     * 返回用户如果Wie
     */
    public static final String NOT_USER = "无";

    /**
     * 不允许把从任务变为待发布
     */
    public static final String suspendTaskFailed = "该任务不允许变为待发布";

    /*
    * 邮件服务提示
    * */
    public static final String ERROR_TO_EMAIL_IS_NULL = "抱歉，未指定要发送的邮箱";
    public static final String ERROR_FROM_EMAIL_IS_NULL = "抱歉，未指定发送者邮箱";
    public static final String ERROR_SEND_ERROR = "抱歉，邮件发送失败";

}
