package swapp.ui;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import swapp.core.SwappItem;
import swapp.core.SwappList;
import swapp.core.SwappModel;
import swapp.json.SwappPersistence;

public class DirectSwappDataAccess implements SwappDataAccess {

    private SwappModel model;

    private final static String swappListWithTwoItems = "{\"lists\":[{\"username\":\"swapp\",\"items\":[{\"itemName\":\"item1\",\"itemUsername\":\"username1\",\"itemStatus\":\"New\",\"itemDescription\":\"info1\"},{\"itemName\":\"item2\",\"itemUsername\":\"username2\",\"itemStatus\":\"New\",\"itemDescription\":\"info2\"}]}]}";

    private String fileName;

    private SwappPersistence swappPersistence = new SwappPersistence();

    private File file;

    public void setModel(SwappModel model) {
        this.model = model;
    }

    public DirectSwappDataAccess(String fileName) throws IOException {
        this.fileName = fileName;
        file = Paths.get(System.getProperty("user.home"), this.fileName).toFile();
        readData();

    }

    public void readData() throws IOException {
        try (Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            this.model = swappPersistence.readSwappModel(reader);
        } catch (IOException e) {
            Reader reader = new StringReader(swappListWithTwoItems);
            model = swappPersistence.readSwappModel(reader);
            System.out.println("Couldn't read default-todomodel.json, so rigging TodoModel manually (" + e + ")");
        }
    }

    @Override
    public void writeData() {
        Writer writer = null;
        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
            swappPersistence.writeSwappModel(model, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Collection<SwappItem> getAllSwappItems() {
        return this.model.getSwappItems();
    }

    @Override
    public void addSwappItem(SwappItem swappItem) throws Exception {
        this.model.addSwappItem(swappItem);
    }

    @Override
    public void removeSwappItem(SwappItem swappItem) throws Exception {
        this.model.removeSwappItem(swappItem);
    }

    @Override
    public void changeSwappItem(SwappItem newItem) throws Exception {
        this.model.changeSwappItem(newItem.getUsername(), getSwappItem(newItem), newItem);
    }

    @Override
    public void removeAllSwappItems(String username) {
        this.model.addNewSwappList(username);
    }

    @Override
    public void addNewSwappList(String username) {
        this.model.addNewSwappList(username);

    }

    @Override
    public List<SwappItem> getSwappItemByUser(String username) {
        return this.model.getSwappItemsByUser(username);
    }

    @Override
    public List<SwappItem> getSwappItemByStatus(String status) {
        return this.model.getSwappItemsByStatus(status);
    }

    @Override
    public boolean isItemChanged(SwappItem swappItem) {
        return this.model.isItemChanged(swappItem);
    }

    @Override
    public boolean hasSwappList(String username) {
        return this.model.hasSwappList(username);
    }

    @Override
    public Collection<SwappList> getAllSwappLists() {
        return this.model.getSwappLists();
    }

    @Override
    public SwappItem getSwappItem(SwappItem swappItem) {
        return this.model.getSwappItem(swappItem);
    }

    @Override
    public boolean hasSwappItem(String username, String itemname) {
        return this.model.hasSwappItem(username, itemname);
    }

}