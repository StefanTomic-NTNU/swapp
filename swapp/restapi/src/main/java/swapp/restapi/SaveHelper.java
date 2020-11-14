package swapp.restapi;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import swapp.core.SwappModel;
import swapp.json.SwappPersistence;

public class SaveHelper {
    private File file;

    public SwappModel read(){
      SwappPersistence swappPersistence = new SwappPersistence();
      try (Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
        return swappPersistence.readSwappModel(reader);
      } catch (IOException e) {
        System.out.println("Couldn't read default-todomodel.json, so rigging TodoModel manually (" + e + ")");
      }
      return new SwappModel();
    }

    public void write(SwappModel model){
      Writer writer = null;
      SwappPersistence swappPersistence = new SwappPersistence();
      try {
        writer = new FileWriter(file, StandardCharsets.UTF_8);
        swappPersistence.writeSwappModel(model, writer);
      } catch (IOException ioex) {
        System.err.println("Feil med fillagring.");
      } finally {
        try {
          if (writer != null) {
            writer.close();
          }
        } catch (IOException e) {
          System.err.println("Feil med fillagring..");
        }
      }
    }

    public void setFilePath(String file){
      this.file = Paths.get(System.getProperty("user.home"), file).toFile();
    }
}
