package com.cs.workwechat.constants;

/**
 * @author chensong
 */
public interface WorkWechatOpenApi {


    /**
     * 获取access_token
     * <p>
     * 获取access_token是调用企业微信API接口的第一步，相当于创建了一个登录凭证，其它的业务API接口，都需要依赖于access_token来鉴权调用者身份。
     * 因此开发者，在使用业务接口前，要明确access_token的颁发来源，使用正确的access_token。
     * <p>
     * 请求方式： GET（HTTPS）
     * 请求地址： https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ID&corpsecret=SECRET
     * 注：此处标注大写的单词ID和SECRET，为需要替换的变量，根据实际获取值更新。其它接口也采用相同的标注，不再说明。
     * <p>
     * 参数说明：
     * <p>
     * 参数	必须	说明
     * corpid	是	企业ID，获取方式参考：术语说明-corpid
     * corpsecret	是	应用的凭证密钥，获取方式参考：术语说明-secret
     * 权限说明：
     * 每个应用有独立的secret，所以每个应用的access_token应该分开来获取
     */
    String GET_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

    /**
     * 获取部门列表
     * <p>
     * 请求方式：GET（HTTPS）
     * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=ACCESS_TOKEN&id=ID
     * <p>
     * 参数说明 ：
     * <p>
     * 参数	必须	说明
     * access_token	是	调用接口凭证
     * id	否	部门id。获取指定部门及其下的子部门。 如果不填，默认获取全量组织架构
     */
    String DEPARTMENT_LIST = "https://qyapi.weixin.qq.com/cgi-bin/department/list";

    /**
     * 获取部门成员详情
     * <p>
     * 请求方式：GET（HTTPS）
     * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD
     * <p>
     * 参数说明：
     * <p>
     * 参数	必须	说明
     * access_token	是	调用接口凭证
     * department_id	是	获取的部门id
     * fetch_child	否	1/0：是否递归获取子部门下面的成员
     */
    String DEPARTMENT_MEMBER_DETAIL = "https://qyapi.weixin.qq.com/cgi-bin/user/list";


    /**
     * 获取客户列表
     * 企业可通过此接口获取指定成员添加的客户列表。客户是指配置了客户联系功能的成员所添加的外部联系人。没有配置客户联系功能的成员，所添加的外部联系人将不会作为客户返回。
     * <p>
     * 请求方式：GET（HTTPS）
     * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/externalcontact/list?access_token=ACCESS_TOKEN&userid=USERID
     * <p>
     * 参数说明：
     * <p>
     * 参数	必须	说明
     * access_token	是	调用接口凭证
     * userid	是	企业成员的userid
     */
    String EXTERNAL_CONTACT_LIST = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/list";

    /**
     * 获取客户详情
     * <p>
     * 企业可通过此接口，根据外部联系人的userid（如何获取?），拉取客户详情。
     * <p>
     * 请求方式：GET（HTTPS）
     * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get?access_token=ACCESS_TOKEN&external_userid=EXTERNAL_USERID
     * <p>
     * 参数说明：
     * <p>
     * 参数	必须	说明
     * access_token	是	调用接口凭证
     * external_userid	是	外部联系人的userid，注意不是企业成员的帐号
     */
    String EXTERNAL_CONTACT_DETAIL = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get";


    /**
     * 获取客户群列表
     * 调试工具
     * 该接口用于获取配置过客户群管理的客户群列表。
     * <p>
     * 请求方式：POST（HTTPS）
     * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/list?access_token=ACCESS_TOKEN
     */
    String EXTERNAL_CONTACT_GROUP_CHAT_LIST = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/list";


    /**
     * 获取客户群详情
     * <p>
     * 通过客户群ID，获取详情。包括群名、群成员列表、群成员入群时间、入群方式。（客户群是由具有客户群使用权限的成员创建的外部群）
     * <p>
     * 请求方式：POST（HTTPS）
     * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/get?access_token=ACCESS_TOKEN
     * <p>
     * 参数说明：
     * <p>
     * {
     * "chat_id":"wrOgQhDgAAMYQiS5ol9G7gK9JVAAAA
     * }
     * 参数	必须	说明
     * access_token	是	调用接口凭证
     * chat_id	是	客户群ID
     */
    String EXTERNAL_CONTACT_GROUP_CHAT_DETAIL = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/get";


    /**
     * 企业可通过此接口获取成员联系客户的数据，包括发起申请数、新增客户数、聊天数、发送消息数和删除/拉黑成员的客户数等指标。
     * <p>
     * 请求方式: POST(HTTP)
     * <p>
     * 请求地址:https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get_user_behavior_data?access_token=ACCESS_TOKEN
     * <p>
     * 请求示例
     * <p>
     * {
     * "userid": [
     * "zhangsan",
     * "lisi"
     * ],
     * "partyid":
     * [
     * 1001,
     * 1002
     * ],
     * "start_time":1536508800,
     * "end_time":1536940800
     * }
     * 参数说明:
     * <p>
     * 参数	必须	说明
     * access_token	是	调用接口凭证
     * userid	否	用户ID列表
     * partyid	否	部门ID列表
     * start_time	是	数据起始时间
     * end_time	是	数据结束时间
     */
    String USER_BEHAVIOR_DATA = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get_user_behavior_data";


    /**
     * 请求方式：POST（HTTPS）
     * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get_unassigned_list?access_token=ACCESS_TOKEN
     * <p>
     * 请求示例：
     * <p>
     * {
     * "page_id":0,
     * "page_size":100
     * }
     * 参数说明：
     * <p>
     * 参数	必须	说明
     * access_token	是	调用接口凭证
     * page_id	否	分页查询，要查询页号，从0开始
     * page_size	否	每次返回的最大记录数，默认为1000，最大值为1000
     */
    String GET_UNASSIGNED_LIST = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get_unassigned_list";


    /**
     * 请求方式：POST（HTTPS）
     * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/externalcontact/transfer?access_token=ACCESS_TOKEN
     * <p>
     * 请求示例：
     * <p>
     * {
     * "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACAAAA",
     * "handover_userid": "zhangsan",
     * "takeover_userid": "lisi"
     * }
     * 参数说明：
     * <p>
     * 参数	必须	说明
     * access_token	是	调用接口凭证
     * external_userid	是	外部联系人的userid，注意不是企业成员的帐号
     * handover_userid	是	离职成员的userid
     * takeover_userid	是	接替成员的userid
     */
    String MEMER_USER_TRANSFER = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/transfer";


    /**
     * 请求方式：POST（HTTPS）
     * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/transfer?access_token=ACCESS_TOKEN
     * <p>
     * {
     * "chat_id_list" : ["wrOgQhDgAAcwMTB7YmDkbeBsgT_AAAA", "wrOgQhDgAAMYQiS5ol9G7gK9JVQUAAAA"],
     * "new_owner" : "zhangsan"
     * }
     * 参数说明：
     * <p>
     * 参数	必须	说明
     * access_token	是	调用接口凭证
     * chat_id_list	是	需要转群主的客户群ID列表。取值范围： 1 ~ 100
     * new_owner	是	新群主ID
     */
    String CHAT_GROUP_TRANSFER = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/transfer";

    /**
     * 调试工具
     * 获取指定日期全天的统计数据。注意，企业微信仅存储180天的数据。
     * <p>
     * 请求方式：POST（HTTPS）
     * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/statistic?access_token=ACCESS_TOKEN
     * {
     * "day_begin_time": 1572505490,
     * "owner_filter": {
     * "userid_list": ["zhangsan"],
     * "partyid_list": [7]
     * },
     * "order_by": 2,
     * "order_asc": 0,
     * "offset" : 0,
     * "limit" : 1000
     * }
     * access_token	是	调用接口凭证
     * day_begin_time	是	开始时间，填当天开始的0分0秒（否则系统自动处理为当天的0分0秒）。取值范围：昨天至前180天
     * owner_filter	否	群主过滤，如果不填，表示获取全部群主的数据
     * userid_list	否	群主ID列表。最多100个
     * partyid_list	否	群主所属部门ID列表。最多100个
     * order_by	否	排序方式。
     * 1 - 新增群的数量
     * 2 - 群总数
     * 3 - 新增群人数
     * 4 - 群总人数
     * <p>
     * 默认为1
     * order_asc	否	是否升序。0-否；1-是。默认降序
     * offset	否	分页，偏移量, 默认为0
     * limit	否	分页，预期请求的数据量，默认为500，取值范围 1 ~ 1000
     */
    String GROUP_CHAT_STATISTIC = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/statistic?access_token=%s";

    /**
     * 企业微信成功状态码
     */
    Integer SUCCESS_CODE = 0;

    Integer NUMBER_ZERO = 0;
    Integer NUMBER_ONE = 1;

    Integer MAX_USER_LIST = 100;
    Integer MAX_CHAT_LIST = 1000;

}
