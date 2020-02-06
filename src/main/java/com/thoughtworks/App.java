package com.thoughtworks;

import java.util.*;

public class App {

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    System.out.println("请点餐（菜品Id x 数量，用逗号隔开）：");
    String selectedItems = scan.nextLine();
    String summary = bestCharge(selectedItems);
    System.out.println(summary);
  }

  /**
   * 接收用户选择的菜品和数量，返回计算后的汇总信息
   *
   * @param selectedItems 选择的菜品信息
   */
  public static String bestCharge(String selectedItems) {
    ArrayList<Integer> itemArr = preTreat(selectedItems);
    String[] itemNameArr = getItemNames();
    double[] itemPriceArr = getItemPrices();
    double totalPrice = calculateTotalPrice(itemArr);
    double decreaseHalfPrice = calculateDecreaseHalf(itemArr);
    double decreaseFixedPrice = calculateDecreaseFixedPrice(totalPrice);
    boolean isCheaper = isCheap(decreaseHalfPrice,decreaseFixedPrice);
    double finalPrice;
    boolean chooseWhichPlan = chooseWhichPlan(decreaseHalfPrice,decreaseFixedPrice);
    if(!isCheaper){
      finalPrice = totalPrice;
    }else{
      finalPrice = calculateFinalPrice(totalPrice,decreaseHalfPrice,decreaseFixedPrice,chooseWhichPlan);
    }
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("============= 订餐明细 =============\n");
    for(int i=0;i<itemArr.size();i+=2){
      String itemName = itemNameArr[itemArr.get(i)];
      double itemPrice = itemPriceArr[itemArr.get(i)];
      int itemNum = itemArr.get(i+1);
      int oneItemTotalPrice = new Double(itemNum * itemPrice).intValue();
      stringBuilder.append(String.format("%s x %d = %d元%n",itemName,itemNum,oneItemTotalPrice));
    }
    stringBuilder.append("-----------------------------------\n");
    if(isCheaper){
      stringBuilder.append("使用优惠:\n");
      if(chooseWhichPlan){
        stringBuilder.append(String.format("指定菜品半价(黄焖鸡，凉皮)，省%d元%n",new Double(decreaseHalfPrice).intValue()));
      }else{
        stringBuilder.append("满30减6元，省6元\n");
      }
      stringBuilder.append("-----------------------------------\n");
    }
    stringBuilder.append(String.format("总计：%d元%n",new Double(finalPrice).intValue()));
    stringBuilder.append("===================================");
    return stringBuilder.toString();
  }

  public static ArrayList preTreat(String selectedItems) {
    ArrayList<Integer> itemArr = new ArrayList<Integer>();
    String[] totalItemString = selectedItems.split(",");
    for (int i = 0; i < totalItemString.length; i++) {
      String[] everyItemString = totalItemString[i].split(" x ");
      String itemName = everyItemString[0];
      int itemNum = Integer.valueOf(everyItemString[1]);
      String[] itemId = getItemIds();
      int itemIndex = 0;
      for (int j = 0; j < itemId.length; j++) {
        if (itemName.equals(itemId[j])) {
          itemIndex = j;
          break;
        }
      }
      itemArr.add(itemIndex);
      itemArr.add(itemNum);
    }
    return itemArr;
  }

  public static double calculateTotalPrice(ArrayList<Integer> itemArr){
    int totalPrice = 0;
    double[] itemPrice = getItemPrices();
    for(int i =0;i<itemArr.size();i+=2){
      int index = itemArr.get(i);
      int number = itemArr.get(i+1);
      totalPrice += itemPrice[index] * number;
    }
    return totalPrice;
  }

  public static double calculateDecreaseHalf(ArrayList<Integer> itemArr){
    int decreasePrice = 0;
    double[] itemPrice = getItemPrices();
    for(int i =0;i<itemArr.size();i++){
      if(itemArr.get(i)==0){
        double everyDecrease = itemPrice[0] /2 ;
        decreasePrice += everyDecrease * itemArr.get(i+1);
      }else if(itemArr.get(i)==2){
        double everyDecrease = itemPrice[2] /2 ;
        decreasePrice += everyDecrease * itemArr.get(i+1);
      }
      i++;
    }
    return decreasePrice;
  }
  public static double calculateDecreaseFixedPrice(double totalPrice){
    if(totalPrice>=30){
      return 6.0d;
    }else{
      return 0.0d;
    }
  }

  public static boolean isCheap(double decreaseHalfPrice,double decreaseFixedPrice){
    if(decreaseHalfPrice==0&&decreaseFixedPrice==0){
      return false;
    }else {
      return true;
    }
  }

  public static boolean chooseWhichPlan(double decreaseHalfPrice,double decreaseFixedPrice){
    if(decreaseHalfPrice>=decreaseFixedPrice){
      return true;
    }else{
      return false;
    }
  }
  public static double calculateFinalPrice(double totalPrice, double decreaseHalfPrice,double decreaseFixedPrice,boolean chooseWhichPlan){
    if(chooseWhichPlan){
      return totalPrice - decreaseHalfPrice;
    }else{
      return totalPrice - decreaseFixedPrice;
    }
  }

  /**
   * 获取每个菜品依次的编号
   */
  public static String[] getItemIds() {
    return new String[]{"ITEM0001", "ITEM0013", "ITEM0022", "ITEM0030"};
  }

  /**
   * 获取每个菜品依次的名称
   */
  public static String[] getItemNames() {
    return new String[]{"黄焖鸡", "肉夹馍", "凉皮", "冰粉"};
  }

  /**
   * 获取每个菜品依次的价格
   */
  public static double[] getItemPrices() {
    return new double[]{18.00, 6.00, 8.00, 2.00};
  }

  /**
   * 获取半价菜品的编号
   */
  public static String[] getHalfPriceIds() {
    return new String[]{"ITEM0001", "ITEM0022"};
  }
}
