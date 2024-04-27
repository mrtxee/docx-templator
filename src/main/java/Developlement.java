//import com.apache.http.NameValuePair;

public class Developlement {

  public static void main(String[] args){

//    TagData tagData = new TagData();
//    tagData.setName("строкаИмя");
//    tagData.setStringValue("Значение");
//    tagData.getValue1().get(0).get("data");




  }

  public static void stringToValuesCollectionForListTest() {
    String songStr = "{'songs': [{ 'name': 'Memories' },{ 'name': 'Sugar' },{ 'name': 'Last Dance' }]}";
    songStr = "{ 'СписокДел': [ { 'НомерПостановления': '7777' ,'РегистрацияНарушителяГород': 'Ульяновск' }, { "
      + "'НомерПостановления': '888' ,'РегистрацияНарушителяГород': 'Мирный' }, {'РегистрацияНарушителяГород': "
      + "'Омск'} ] }";
    String songStr1 = "[ { 'НомерПостановления': '777' ,'РегистрацияНарушителяГород': 'Ульяновск' }, { "
      + "'НомерПостановления': '888' ,'РегистрацияНарушителяГород': 'Самара' }, {'РегистрацияНарушителяГород': 'Омск'"
      + " } ]";

    songStr="{ \"СписокДел\": [ { \"НомерПостановления\": \"111\" ,\"РегистрацияНарушителяГород\": \"Ульяновск\" }, {"
      + " \"НомерПостановления\": \"222\" ,\"РегистрацияНарушителяГород\": \"Мирный\" } ] }";

//    System.out.println(stringToValuesCollectionForList(songStr,"СписокДел"));
  }

//  private static List<Map<String, String>> stringToValuesCollectionForList(String jsonString, String name){
//    final List<Map<String, String>> valuesCollection = new ArrayList<>();
//    final JSONObject jsonObject;
//    try {
//      jsonObject = new JSONObject(jsonString);
//      if(!jsonObject.has(name)){
//        return valuesCollection;
//      }
//      JSONArray jsonArray = jsonObject.getJSONArray(name);
//      for (int i=0; i<jsonArray.length(); i++) {
//        final JSONObject curJson = (JSONObject) jsonArray.get(i);
//        final Iterator keys = curJson.keys();
//        final Map<String, String> valuesCollectionItem = new HashMap<>();
//        while(keys.hasNext()) {
//          final String key = keys.next().toString();
//          valuesCollectionItem.put(key, curJson.get(key).toString());
//        }
//        valuesCollection.add(valuesCollectionItem);
//      }
//    } catch (JSONException e) {
//      throw new RuntimeException(e);
//    }
//    return valuesCollection;
//  }

  public static void regexEx(){
    String regex = "^@.+$";
    System.out.println("@lala".matches(regex)); // true
    System.out.println("@".matches(regex)); // false
    System.out.println("lala".matches(regex)); // false
    System.out.println("".matches(regex)); // false

    System.out.println("@lala".replaceFirst("@",""));
    System.out.println("@lala@@".replaceFirst("@",""));
    System.out.println("@".replaceFirst("@",""));
    System.out.println("lala".replaceFirst("@",""));
    System.out.println("".replaceFirst("@",""));

  }

}
