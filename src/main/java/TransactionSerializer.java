import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kanes on 27.12.2017.
 */
public class TransactionSerializer implements JsonSerializer {

    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();

        TransactionP t = (TransactionP) o;

        jsonObject.addProperty("id", t.getId());

        JsonObject quantity = new JsonObject();

        Map<Integer, Integer> res = new HashMap<>();

        for(Product p: t.getQuantity().keySet())
            res.put(p.getProdId(), t.getQuantity().get(p));

        for(Integer id: res.keySet())
            quantity.addProperty(Integer.toString(id), res.get(id));

        final JsonElement quantityJ= jsonSerializationContext.serialize(quantity);

        jsonObject.add("quantity", quantityJ);



        return jsonObject;

    }
}
