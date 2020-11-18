package swapp.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import swapp.core.SwappModel;

public class SwappPersistence {

  private ObjectMapper mapper;

  public SwappPersistence() {
    mapper = new ObjectMapper();
    mapper.registerModule(new SwappModule());
  }

  public SwappModel readSwappModel(Reader reader) throws IOException {
    return mapper.readValue(reader, SwappModel.class);
  }

  public void writeSwappModel(SwappModel swappModel, Writer writer) throws IOException {
    mapper.writeValue(writer, swappModel);
  }

}
