local M = {}

function M.gunsmithlib_on_put_away(api)
    if (api:getAmmoAmount() == 0 and not api:hasAmmoInBarrel()) then
        local ext = api:gunsmithlib_extension()
        ext:unload_all_attachments()
        ext:discard_weapon()
    end
end

return M