-- Extends tacz_xmag_reload_logic.lua
local M = setmetatable({}, {__index = require("tacz_xmag_reload_logic")})
local INACCURACY_COEFFICIENT = 8

function M.calcSpread(api, _i, inaccuracy)
    local params = api:getScriptParams()
    local angle_range = params.wasp_angle_range
    local slope = math.sin(math.rad(params.wasp_fov))

    local ext = api:gunsmithlib_extension()
    local rng = ext:shooter_states():get_random_generator()
    local angle = math.rad(rng:nextDouble(-angle_range, angle_range) + 90);
    local x = math.cos(angle) * slope * INACCURACY_COEFFICIENT + rng:nextDouble(inaccuracy)
    local y = math.sin(angle) * slope * INACCURACY_COEFFICIENT + rng:nextDouble(inaccuracy)
    return { x, y }
end

return M