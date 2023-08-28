# tiktok-project
仿抖音项目-Java重写版

### 评论模块设计：

#### 获取视频评论缓存问题：

要想获取视频的评论缓存，最简单的做法就是用该视频的id作为key，评论列表作为value存入redis中。每次有新的用户发布评论就将对应视频的评论缓存全部删除，生成新的评论列表存入进行更新。

如果这个视频非常火，用户评论数在短时间内剧增，也就出现了一些问题，：

1. 缓存列表需要不断更新，缓存的命中率就十分的低，这种设计导致缓存意义不大。
2. 频繁的序列化和反序列化：因为将对象从内存转化为二进制或json格式存入redis中需要消耗cpu资源进行序列化，频繁的更新增大了cpu的压力
3. 随着评论数据量的增大，可能形成大key，不仅对redis服务有影响，反序列化出来的评论列表也就越大，有内存溢出的风险

#### 解决思路：

首先我们肯定不能总是一次性把所有评论都拿出来，可以先拿比如说300条，等到滑动到底了再触发请求再拿300条存入缓存。但是我们返回给前端是20条

我们把这300条数据只取出主键索引，也就是评论id按评论时间升序存入redis的一个zset中【1-300】，让value内容可以减少。这个列表称为【评论id列表缓存】。如果拿出下一批就是【301-600】，key为：` index-301_600`。

如果又来了一条新的，【601-？？？】，前端拿肯定是拿不到20条，那就再往前面的zset也就是【index-301-600】再拿20条，此时会拿出20条+的数据。同时新增一条K-V的具体评论内容

对于具体的评论内容，我们通过它的评论id和评论内容按K-V缓存来处理（string类型），拿到【评论id列表缓存】后遍历K获取每一条具体内容V。

每次需要更新缓存的时候我们就只需要更新【评论id列表缓存】，具体的string没必要变动

#### 新增评论后的缓存流程：

1. 用户insert一条评论，清空该视频的【评论id列表缓存】，具体评论内容的string保留着
2. 重新select出最新的300条评论，300个评论id存入新的【评论id列表缓存】，300条具体评论内容
3. 返回前20条数据给前端

#### 设计中仍存在的问题：

1. 如果拿一个【评论id列表缓存】的区间段数据不满足20条时会拿上一个区间段，为了减少下标的索引计算就索性再拿20条数据了。但这样就会导致每次拿到的数据条数不一定是20条，但至少始终是在【0-40】之间
2. 随着数据量的增加，内存的占用肯定会不可避免的增高，这时可以将序列化的格式由json改为protobuf



### （待更）

