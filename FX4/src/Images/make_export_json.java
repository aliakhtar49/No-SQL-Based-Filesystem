package Images;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class make_export_json {
	static JSONObject f = new JSONObject();

	public static void main(String[] args) {
		JSONArray d = new JSONArray();
		d.add("d");
		d.add("h");
		JSONObject a= new JSONObject();
		a.put("chunks", d);
		
		JSONObject b= new JSONObject();
		JSONArray e = new JSONArray();
		e.add("di");
		e.add("hi");
		b.put("files", e);
		
		JSONObject c = new JSONObject();
		c.put("chunks", d);
		c.put("files", e);
		
		
		
		f.put(0, c);
		System.out.println(f);
		JSONObject v = (JSONObject) f;
		
		JSONObject w = (JSONObject) v.get(0);
		JSONArray x = (JSONArray) w.get("chunks");
		x.add("d");
		
		
	
		System.out.println(f);
		
		
		
		
		//Add new Calue to chunks
		
//		JSONObject final_one = new JSONObject();
//		final_one.put("file_type", "folder");
//		final_one.put("start_level", 0);
//		final_one.put("total_level", 4);
//		JSONObject temp = new JSONObject();
//		temp.put("chunks", "chunks-value");
//		JSONObject temp5= new JSONObject();
//		temp5.put("chunks", "chunks-value");
//		temp5.put("files", "files-value");
//		JSONArray a = new JSONArray();
//		a.add(temp);
//		a.add(temp5);
//		
//		
//		JSONObject temp1 = new JSONObject();
//		temp1.put("files", "files-value");
//		
//
//		JSONObject temp3 = new JSONObject();
//		temp3.put("files", "files-value");
//		JSONArray b = new JSONArray();
//		b.add(temp1);
//		b.add(temp3);
//		  b.addAll(a);  
//		
//		
//
//		final_one.put(0, b);
	
		
		// TODO Auto-generated method stub

	}

}
