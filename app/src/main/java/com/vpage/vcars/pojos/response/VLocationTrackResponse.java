package com.vpage.vcars.pojos.response;

import com.vpage.vcars.pojos.VLocationTrack;
import java.util.Arrays;

public class VLocationTrackResponse {

    private VLocationTrack[] Location;

    public VLocationTrack[] getLocation() {
        return Location;
    }

    public void setLocation(VLocationTrack[] location) {
        Location = location;
    }

    @Override
    public String toString() {
        return "VLocationTrackResponse{" +
                "Location=" + Arrays.toString(Location) +
                '}';
    }
}
