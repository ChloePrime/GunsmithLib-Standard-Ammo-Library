-- 脚本的位置是 "{命名空间}:{路径}"，那么 require 的格式为 "{命名空间}_{路径}"
-- 注意！require 取得的内容不应该被修改，应仅调用
local default = require("tacz_default_state_machine")
local STATIC_TRACK_LINE = default.STATIC_TRACK_LINE
local MAIN_TRACK = default.MAIN_TRACK
local main_track_states = default.main_track_states

local idle_state = setmetatable({}, {__index = main_track_states.idle})
local gun_kick_state = setmetatable({}, {__index = default.gun_kick_state})

function gun_kick_state.transition(this, context, input)
    -- 玩家按下开火键时需要在射击轨道行里寻找空闲轨道去播放射击动画(如果没有空闲会分配新的),需要注意的是射击动画要向下混合
    if (input == INPUT_SHOOT) then
        local ext = context:gunsmithlib_extension()
        local states = ext:shooter_states()

        local is_scoping = states:aiming_progress() >= 0.9 and states:get_scope_type() == "SCOPE"
        local anim = ext:ternary_op(is_scoping, "shoot", "shoot2")
        -- 这里是混合动画，一般是可叠加的 gun kick
        local track = context:findIdleTrack(GUN_KICK_TRACK_LINE, false)
        context:runAnimation(anim, track, true, PLAY_ONCE_STOP, 0)
        return nil
    end
    return default.gun_kick_state.transition(this, context, input)
end

-- 检查当前是否还有弹药
local function isNoAmmo(context)
    -- 这里同时检查了枪管和弹匣
    return (not context:hasBulletInBarrel()) and (context:getAmmoCount() <= 0)
end

local function runReloadAnimation(context)
    -- 此处获取的轨道是位于主轨道行上的主轨道
    local track = context:getTrack(STATIC_TRACK_LINE, MAIN_TRACK)
    local ext = context:getMagExtentLevel()
    -- 根据当前整枪内是否还有弹药决定是播放战术换弹还是空枪换弹
    if (isNoAmmo(context)) then
        -- "ext"表示扩容插件等级,"0"代表不装扩容插件
        if (ext < 2) then
            context:runAnimation("reload_empty_2", track, false, PLAY_ONCE_STOP, 0.2)
        else
            context:runAnimation("reload_empty", track, false, PLAY_ONCE_STOP, 0.2)
        end
    else
        if (ext < 2) then
            context:runAnimation("reload_tactical_2", track, false, PLAY_ONCE_STOP, 0.2)
        else
            context:runAnimation("reload_tactical", track, false, PLAY_ONCE_STOP, 0.2)
        end
    end
end

function idle_state.transition(this, context, input)
    if (input == INPUT_RELOAD) then
        runReloadAnimation(context)
        return this.main_track_states.idle
    end
    return main_track_states.idle.transition(this, context, input)
end

-- 用元表的方式继承默认状态机的属性
local M = setmetatable({
    main_track_states = setmetatable({
        idle = idle_state
    }, {__index = main_track_states}),
    gun_kick_state = gun_kick_state
}, {__index = default})

function M:initialize(context)
    default.initialize(self, context)
end

-- 继承默认状态机需要重新初始化状态
function M:states()
    return {
        self.base_track_state,
        self.bolt_caught_states.normal,
        self.over_heat_states.normal,
        self.main_track_states.start,
        self.gun_kick_state,
        self.movement_track_states.idle,
        self.ADS_states.normal,
        self.slide_states.normal
    }
end

-- 导出状态机
return M