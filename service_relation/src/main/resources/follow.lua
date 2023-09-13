-- 关注用户的逻辑
-- 将被关注者和关注者添加进相应的key集合中
local userId = ARGV[1]
local toUserId = ARGV[2]

local followUserIdKey = "followUserIds:" .. userId
local followerIdKey = "followerIds:" .. toUserId

-- 使用 pcall 来执行第一个命令
local success1, error1 = pcall(redis.call, 'sadd', followUserIdKey, toUserId)

-- 检查第一个命令是否执行成功
if not success1 then
    return 1  -- 返回错误消息
end

-- 使用 pcall 来执行第二个命令
local success2, error2 = pcall(redis.call, 'sadd', followerIdKey, userId)

-- 检查第二个命令是否执行成功
if not success2 then
    return 2  -- 返回错误消息
end


-- 操作无误则返回0
return 0
