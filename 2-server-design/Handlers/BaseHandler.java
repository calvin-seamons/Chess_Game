package Handlers;

public abstract class BaseHandler {

    protected String createAuthToken() {
        // The same createAuthToken code you provided
        StringBuilder authToken = new StringBuilder();
        for(int i = 0; i < 8; i++) {
            int random = (int)(Math.random() * 62);
            if(random < 10) {
                authToken.append(random);
            }
            else if(random < 36) {
                authToken.append((char) (random + 55));
            }
            else {
                authToken.append((char) (random + 61));
            }
        }
        return authToken.toString();
    }

    // Any other common methods can go here

}