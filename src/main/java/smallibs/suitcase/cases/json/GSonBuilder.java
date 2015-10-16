/*
 * Copyright (C)2015 D. Plaindoux.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; see the file COPYING.  If not, write to
 * the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package smallibs.suitcase.cases.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import smallibs.suitcase.utils.Option;
import smallibs.suitcase.utils.Pair;

public class GSonBuilder implements JSonHandler<JsonElement, JsonObject, Pair<String, JsonElement>, JsonArray, JsonElement> {
    @Override
    public JsonElement anObject(Option<JsonObject> members) {
        if (members.isNone()) {
            return new JsonObject();
        } else {
            return members.value();
        }
    }

    @Override
    public JsonArray anArray(Option<JsonArray> values) {
        if (values.isNone()) {
            return new JsonArray();
        } else {
            return values.value();
        }
    }

    @Override
    public JsonObject someMembers(Pair<String, JsonElement> o1, Option<JsonObject> o2) {
        final JsonObject objectMap;
        if (o2.isNone()) {
            objectMap = new JsonObject();
        } else {
            objectMap = o2.value();
        }

        objectMap.add(o1._1, o1._2);

        return objectMap;
    }

    @Override
    public Pair<String, JsonElement> aMember(String o1, JsonElement o2) {
        return new Pair<>(o1, o2);
    }

    @Override
    public JsonArray someValues(JsonElement o1, Option<JsonArray> o2) {
        final JsonArray objectList = new JsonArray();

        objectList.add(o1);

        if (o2.isPresent()) {
            objectList.addAll(o2.value());
        }

        return objectList;
    }

    @Override
    public JsonElement anInteger(int i) {
        return new JsonPrimitive(i);
    }

    @Override
    public JsonElement aString(String s) {
        return new JsonPrimitive(s);
    }

    @Override
    public JsonElement aFloat(float f) {
        return new JsonPrimitive(f);
    }

    @Override
    public JsonElement aNull() {
        return JsonNull.INSTANCE;
    }

    @Override
    public JsonElement aBoolean(boolean b) {
        return new JsonPrimitive(b);
    }

    @Override
    public JsonElement aValue(JsonElement element) {
        return element;

    }
}
