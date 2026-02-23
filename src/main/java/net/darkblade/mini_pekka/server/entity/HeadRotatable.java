package net.darkblade.mini_pekka.server.entity;

public interface HeadRotatable {
    float getCachedHeadYaw();
    void setCachedHeadYaw(float yaw);
    float getCachedHeadPitch();
    void setCachedHeadPitch(float pitch);
}
