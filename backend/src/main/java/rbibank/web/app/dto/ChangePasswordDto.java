package rbibank.web.app.dto;

public class ChangePasswordDto {
    private String currentPassword;
    private String newPassword;

    @java.lang.SuppressWarnings("all")
    public String getCurrentPassword() {
        return this.currentPassword;
    }

    @java.lang.SuppressWarnings("all")
    public String getNewPassword() {
        return this.newPassword;
    }

    @java.lang.SuppressWarnings("all")
    public void setCurrentPassword(final String currentPassword) {
        this.currentPassword = currentPassword;
    }

    @java.lang.SuppressWarnings("all")
    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }
}
