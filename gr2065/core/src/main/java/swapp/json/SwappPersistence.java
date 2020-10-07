package swapp.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import swapp.core.SwappItemList;

public class SwappPersistence {

  private ObjectMapper mapper;

  public SwappPersistence() {
    mapper = new ObjectMapper();
    mapper.registerModule(new SwappItemModule());
  }

  public SwappItemList readSwappList(Reader reader) throws IOException {
    return mapper.readValue(reader, SwappItemList.class);
  }

  public void writeSwappList(SwappItemList swappList, Writer writer) throws IOException {
    mapper.writeValue(writer, swappList);
  }

}
