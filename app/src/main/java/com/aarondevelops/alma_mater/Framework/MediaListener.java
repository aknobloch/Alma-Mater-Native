package com.aarondevelops.alma_mater.Framework;


public interface MediaListener
{
    public void publishSongState(int progress);

    public void reset();

    public void publishSongDuration(int duration);
}
