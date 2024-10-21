package it.fostidich.caster;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static it.fostidich.caster.Errors.*;

public class Json {
    public static <T> T fromJsonFile(Class<T> c, String resourcePath) {
        T result = null;
        try (Reader reader = new InputStreamReader(
                Resources.getResourceStream(resourcePath))) {
            Gson gson = new Gson();
            result = gson.fromJson(reader, c);
        } catch (IOException ignored) {
            ResourceNotFound.abort(resourcePath);
        } catch (Exception ignored) {
            JsonParserFailure.abort(resourcePath + " produced json error");
        }
        return result;
    }
}
