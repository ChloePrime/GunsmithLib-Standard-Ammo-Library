-- Extends tacz_xmag_reload_logic.lua
local M = setmetatable({}, { __index = require("tacz_xmag_reload_logic") })

function M.modify_property(api, id, original)
    if (id == "gunsmithlib_programmed_airburst_distance") then
        local params = api:getScriptParams()
        return original + params.ab_offset
    else
        return original
    end
end

return M
