package kesslercascade.rdpmouse;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class RDPMousePreLaunch implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        RDPMouseCore.init();
    }
}
