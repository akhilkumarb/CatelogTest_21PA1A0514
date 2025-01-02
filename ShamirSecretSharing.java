import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ShamirSecretSharing {
   public static Map<Integer, BigInteger> parseAndDecode(JSONObject var0) {
      HashMap var1 = new HashMap();
      JSONObject var2 = (JSONObject) var0.get("keys");
      if (var2 == null) throw new IllegalArgumentException("JSON must contain a 'keys' object with 'n' and 'k'.");
      Long var3 = (Long) var2.get("n");
      if (var3 == null) throw new IllegalArgumentException("The 'keys' object must contain 'n'.");
      int var4 = var3.intValue();
      for (int var5 = 1; var5 <= var4; ++var5) {
         JSONObject var6 = (JSONObject) var0.get(String.valueOf(var5));
         if (var6 == null) System.err.println("Warning: Root " + var5 + " is missing in the JSON file. Skipping...");
         else {
            try {
               String var8 = (String) var6.get("base");
               String var9 = (String) var6.get("value");
               if (var8 != null && var9 != null) {
                  int var10 = Integer.parseInt(var8);
                  BigInteger var11 = new BigInteger(var9, var10);
                  var1.put(var5, var11);
               } else System.err.println("Warning: Missing base or value for root " + var5 + ". Skipping...");
            } catch (NumberFormatException var12) {
               System.err.println("Error decoding root " + var5 + ": " + var12.getMessage());
            }
         }
      }
      return var1;
   }

   public static BigInteger findConstantTerm(Map<Integer, BigInteger> var0, int var1) {
      ArrayList var2 = new ArrayList(var0.entrySet());
      if (var2.size() < var1) throw new IllegalArgumentException("Not enough roots to solve the polynomial. Expected at least " + var1 + " roots.");
      BigInteger var3 = BigInteger.ZERO;
      for (int var4 = 0; var4 < var1; ++var4) {
         int var5 = (Integer) ((Map.Entry) var2.get(var4)).getKey();
         BigInteger var6 = (BigInteger) ((Map.Entry) var2.get(var4)).getValue();
         BigInteger var7 = var6;
         for (int var8 = 0; var8 < var1; ++var8) {
            if (var4 != var8) {
               int var9 = (Integer) ((Map.Entry) var2.get(var8)).getKey();
               var7 = var7.multiply(BigInteger.valueOf(-var9)).divide(BigInteger.valueOf(var5 - var9));
            }
         }
         var3 = var3.add(var7);
      }
      return var3;
   }

   public static void main(String[] var0) {
      JSONParser var1 = new JSONParser();
      try {
         JSONObject var2 = (JSONObject) var1.parse(new FileReader("testcase1.json"));
         JSONObject var3 = (JSONObject) var1.parse(new FileReader("testcase2.json"));
         Map var4 = parseAndDecode(var2);
         Map var5 = parseAndDecode(var3);
         JSONObject var6 = (JSONObject) var2.get("keys");
         JSONObject var7 = (JSONObject) var3.get("keys");
         if (var6 == null || var7 == null) throw new IllegalArgumentException("Both test cases must contain a 'keys' object.");
         int var8 = ((Long) var6.get("k")).intValue();
         int var9 = ((Long) var7.get("k")).intValue();
         BigInteger var10 = findConstantTerm(var4, var8);
         BigInteger var11 = findConstantTerm(var5, var9);
         System.out.println("Constant term for TestCase 1: " + String.valueOf(var10));
         System.out.println("Constant term for TestCase 2: " + String.valueOf(var11));
      } catch (IOException var12) {
         System.err.println("Error reading JSON file: " + var12.getMessage());
      } catch (ParseException var13) {
         System.err.println("Error parsing JSON file: " + var13.getMessage());
      } catch (IllegalArgumentException var14) {
         System.err.println("Input error: " + var14.getMessage());
      } catch (ArithmeticException var15) {
         System.err.println("Math error: " + var15.getMessage());
      }
   }
}
