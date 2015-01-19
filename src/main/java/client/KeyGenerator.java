package client;


import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;

public class KeyGenerator {

    public KeyGenerator() {

    }

    public BigInteger generateSHAint(String string) {
        String hex = DigestUtils.shaHex(string);
   /*     switch (string) {
            case "localhost:8080":
                return new BigInteger("3");
            case "localhost:8081":
                return new BigInteger("4");
            case "localhost:8082":
                return new BigInteger("7");
            case "localhost:8083":
                return new BigInteger("9");
            case "localhost:8084":
                return new BigInteger("12");
            case "localhost:8085":
                return new BigInteger("14");
            case "localhost:8089":
                return new BigInteger("15");
        }*/
        return toBigInt(hex);
    }

    public BigInteger toBigInt(String hex) {
        return new BigInteger(hex, 16);
    }

    public static BigInteger printKey(BigInteger key) {
        return key.divide(new BigInteger("5000000000000000000000000000000000000000000000"));
       // return key;
    }
}
