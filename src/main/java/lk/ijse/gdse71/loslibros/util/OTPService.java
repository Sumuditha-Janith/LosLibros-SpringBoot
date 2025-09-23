package lk.ijse.gdse71.loslibros.util;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OTPService {
    private final Map<String, OTPData> otpStorage = new HashMap<>();

    public String generateOTP(String email) {
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(999999));

        otpStorage.put(email, new OTPData(otp, System.currentTimeMillis() + 300000));

        return otp;
    }

    public boolean validateOTP(String email, String otp) {
        OTPData otpData = otpStorage.get(email);
        if (otpData == null) return false;

        if (System.currentTimeMillis() > otpData.getExpiryTime()) {
            otpStorage.remove(email);
            return false;
        }

        boolean isValid = otpData.getOtp().equals(otp);
        if (isValid) {
            otpStorage.remove(email);
        }

        return isValid;
    }

    public void clearOTP(String email) {
        otpStorage.remove(email);
    }

    private static class OTPData {
        private final String otp;
        private final long expiryTime;

        public OTPData(String otp, long expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public long getExpiryTime() {
            return expiryTime;
        }
    }
}