package br.tijela.swordlevelv3.util;

import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.Charset;

public class YamlUTF8 extends YamlConfiguration{

    public static final Charset CHARSET = Charset.forName("UTF-8");

    private File file = null;

    public YamlUTF8() {}

    public YamlUTF8(InputStream stream) throws IOException, InvalidConfigurationException {
        load(stream);
    }

    public YamlUTF8(File file) throws IOException, InvalidConfigurationException {
        this.file = file;
        load(file);
        setFile(file);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void reload() throws IOException, InvalidConfigurationException {
        load(new FileInputStream(file));
    }

    public void load() throws IOException, InvalidConfigurationException {
        load(file);
    }

    @Override
    public void load(File file) throws IOException, InvalidConfigurationException{
        load(new FileInputStream(file));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void load(InputStream stream) throws IOException, InvalidConfigurationException {
        Validate.notNull(stream, "Stream cannot be null!");

        InputStreamReader reader = new InputStreamReader(stream, CHARSET);
        StringBuilder builder = new StringBuilder();

        try (BufferedReader input = new BufferedReader(reader)){

            String line;

            while ((line = input.readLine()) != null){
                builder.append(line);
                builder.append('\n');
            }

        }

        loadFromString(builder.toString());
    }

    public void save() throws IOException{
        if(file != null)
            save(file);
    }

    @Override
    public void save(File file) throws IOException {
        Validate.notNull(file, "File cannot be null!");

        Files.createParentDirs(file);

        FileOutputStream stream = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(stream, CHARSET);

        try {

            writer.write(saveToString());

        } finally {
            writer.close();
        }
    }

}
