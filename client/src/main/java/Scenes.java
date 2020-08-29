public enum Scenes {
    MAIN("ClientView.fxml"), AUTHORISATION("Authorisation.fxml"), REGISTRATION("Registration.fxml");
    private final String root;

    Scenes(String root) {
        this.root = root;
    }

    public String getRoot() {
        return root;
    }
}
