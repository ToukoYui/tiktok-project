-- 取消关注用户的逻辑
-- 将被关注者和关注者添加进相应的key集合中
local userId = ARGV[1]
local toUserId = ARGV[2]

local followUserIdKey = "followUserIds:" .. userId
local followerIdKey = "followerIds:" .. toUserId

-- 执行操作
redis.call('srem',followUserIdKey,toUserId)
redis.call('srem',followerIdKey,userId)

-- 操作无误则返回0
return 0
