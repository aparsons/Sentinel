package net.aparsons.sentinel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.aparsons.sentinel.core.Constants;
import net.aparsons.sentinel.core.Sentinel;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Launcher {
    
    private static Sentinel sentinel;
    
    public static Options getOptions() {
        final Options options = new Options();
        
        final Option keyOption = new Option(Constants.OPTION_SENTINEL_KEY, true, "WhiteHat Sentinel API key");
        keyOption.setRequired(false);
        
        final Option logOption = new Option(Constants.OPTION_SENTINEL_LOG, true, "Log file location");
        logOption.setRequired(false);
        
        final Option syncOption = new Option(Constants.OPTION_SENTINEL_SYNC, false, "Download latest WhiteHat Sentinel data");
        syncOption.setRequired(false);
        
        options.addOption(keyOption);
        options.addOption(syncOption);
        
        return options;
    }
    
    private static void printUsage() {
        new HelpFormatter().printHelp("java -jar Sentinel-" + Constants.VERSION + ".jar [options]", getOptions());
    }
    
    public static void main(String[] args) {
        
        CommandLineParser parser = new GnuParser();
        
        try {
            CommandLine cmdLine = parser.parse(getOptions(), args);

            sentinel = new Sentinel();
            
            if (cmdLine.hasOption(Constants.OPTION_SENTINEL_LOG)) {
                sentinel.setLog(cmdLine.getOptionValue(Constants.OPTION_SENTINEL_LOG));
            }
            
            if (cmdLine.hasOption(Constants.OPTION_SENTINEL_KEY)) {
                sentinel.setKey(cmdLine.getOptionValue(Constants.OPTION_SENTINEL_KEY));
            }
            
            if (sentinel.getKey() == null) {
                System.out.print("Enter your WhiteHat Sentinel API Key: ");

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String key = null;

                try {
                    key = reader.readLine();
                } catch (IOException ioe) {
                    Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, ioe.getMessage(), ioe);
                    System.exit(1);
                }

                sentinel.setKey(key);
            }
            
            if (cmdLine.hasOption(Constants.OPTION_SENTINEL_SYNC)) {
                sentinel.sync();
            }
            
        } catch (NullPointerException npe) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, npe.getMessage(), npe);
            printUsage();
        } catch (ParseException pe) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, pe.getMessage(), pe);
            printUsage();
        }
    }
    
}