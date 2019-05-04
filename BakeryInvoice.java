package com.hexad;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author Sivaji Ay
 * This clss is to generate invoice depends on the ipnut quantity & item.
 *
 * Need to Pass 2 Command Line arguements as "Quantity <space> Valid Code"
 * More information in "doInvoice() method where whole calculations happen!"
 * */
public class BakeryInvoice {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String output;
        String input = scanner.nextLine();
        String[] inputArr = input.split(" ");

        if(inputArr.length == 2) {
            Map<String, Map<Integer, Double>> bakeryMap = new HashMap<String, Map<Integer, Double>>();
            Map<Integer, Double> vsItemMap = new TreeMap<Integer, Double>(Collections.reverseOrder());
            Map<Integer, Double> mbItemMap = new TreeMap<Integer, Double>(Collections.reverseOrder());
            Map<Integer, Double> cfItemMap = new TreeMap<Integer, Double>(Collections.reverseOrder());

            vsItemMap.put(3, 6.99);
            vsItemMap.put(5, 8.99);

            mbItemMap.put(2, 9.95);
            mbItemMap.put(5, 16.95);
            mbItemMap.put(8, 24.95);

            cfItemMap.put(3, 5.95);
            cfItemMap.put(5, 9.95);
            cfItemMap.put(9, 16.99);

            bakeryMap.put("VS5", vsItemMap);
            bakeryMap.put("MB11", mbItemMap);
            bakeryMap.put("CF", cfItemMap);

            try {
                Integer quantity = Integer.parseInt(inputArr[0].toString().trim());
                String code = inputArr[1].toString().trim();
                if (bakeryMap.containsKey(code.toUpperCase())) {
                    output = doInvoice(input, quantity, bakeryMap.get(code.toUpperCase()));
                } else {
                    output = "This item is unavailable in our Bakery!!Invalid Code!! ";
                }
            } catch (NumberFormatException nfe) {
                output = "Invalid Quantity!! ";
            }
        }else{
            output = "Invalid Quantity/ Code!!";
        }
        if(output.length() >0) {
            output = "No packs available!!";
        }

        System.out.println(output);
    }
    /*
     * Calculate Bill -
     * 1. Will check for (input%packs) to be ZERO.
     * 2. If we get zero after doing all checks, we display expected output
     * 3. If we get non-zero after doing all checks, we return "No Packs available!!" message
     * */
    private static  String doInvoice(String input, Integer quantity, Map<Integer, Double> packsMap){
        String outputStr = null;
        Set<Integer> tempSet = packsMap.keySet();
        StringBuilder stringBuilder = new StringBuilder();
        DecimalFormat priceFormat = new DecimalFormat("##.00");
        Currency currency = Currency.getInstance("USD");
        for(Integer j: packsMap.keySet()){
            Boolean skipLoop = false;
            stringBuilder.delete(0, stringBuilder.length());
            Integer k = quantity%j;
            String invoiceStr = quantity/j+" x "+j+" "+currency.getSymbol()+""+packsMap.get(j);
            Double price = packsMap.get(j) * (quantity/j);
            if(k>0 && k!=j){
                for(Integer l: tempSet){
                    if(l != j){
                        if(k%l !=0){
                            continue;
                        }else{
                            String invoiceStrTemp = k/l+" x "+l+" "+currency.getSymbol()+""+ packsMap.get(l);
                            stringBuilder.append(input.toUpperCase()).append(" ").append(currency.getSymbol()).append(priceFormat.format(price+(packsMap.get(l)*(k/l)))).
                                    append("\n\t").append(invoiceStr).append("\n\t").append(invoiceStrTemp);
                            skipLoop = true;
                            break;
                        }
                    }
                }
            }else if(k==0){
                stringBuilder.append(input.toUpperCase()).append(" ").append(currency.getSymbol()).append(priceFormat.format(price)).append("\n\t").append(invoiceStr);
                break;
            }
            if(skipLoop)
                break;
        }
        outputStr = stringBuilder.toString();
        return outputStr;
    }
}
