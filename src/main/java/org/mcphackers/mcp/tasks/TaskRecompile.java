package org.mcphackers.mcp.tasks;

import org.mcphackers.mcp.MCPConfig;
import org.mcphackers.mcp.tasks.info.TaskInfo;
import org.mcphackers.mcp.tools.ProgressInfo;
import org.mcphackers.mcp.tools.Util;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TaskRecompile extends Task {
	private int total;
	private int progress;
	
	private static final int RECOMPILE = 1;
	private static final int COPYRES = 2;

    public TaskRecompile(int side, TaskInfo info) {
        super(side, info);
        this.total = 100;
        this.progress = 0;
	}

	@Override
    public void doTask() throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> ds = new DiagnosticCollector<>();

        Path binPath = Util.getPath(chooseFromSide(MCPConfig.CLIENT_BIN, 		MCPConfig.SERVER_BIN));
        Path srcPath = Util.getPath(chooseFromSide(MCPConfig.CLIENT_SOURCES, 	MCPConfig.SERVER_SOURCES));

        step();
        this.progress = 1;
        Util.deleteDirectoryIfExists(binPath);
        Files.createDirectories(binPath);
        this.progress = 2;

        // Compile side
        if (Files.exists(srcPath)) {
            Iterable<File> src = Files.walk(srcPath).filter(path -> !Files.isDirectory(path) && path.getFileName().toString().endsWith(".java")).map(Path::toFile).collect(Collectors.toList());
            Iterable<String> options = Arrays.asList("-d", MCPConfig.CLIENT_BIN, "-cp", String.join(";", new String[] {MCPConfig.CLIENT_FIXED, MCPConfig.LWJGL, MCPConfig.LWJGL_UTIL, MCPConfig.JINPUT}));
            if(side == SERVER) {
            	options = Arrays.asList("-d", MCPConfig.SERVER_BIN, "-cp", String.join(";", MCPConfig.SERVER));
            }
            this.progress = 3;
            recompile(compiler, ds, src, options);
            this.progress = 50;
            // Copy assets from source folder
            step();
            List<Path> assets = Files.walk(srcPath).filter(path -> !Files.isDirectory(path) && !path.getFileName().toString().endsWith(".java")).collect(Collectors.toList());
            int i = 0;
            for(Path path : assets) {
            	if(srcPath.relativize(path).getParent() != null) {
            		Files.createDirectories(Paths.get(binPath.toString(), srcPath.relativize(path).getParent().toString()));
            	}
            	Files.copy(path, Paths.get(binPath.toString(), srcPath.relativize(path).toString()));
            	i++;
            	this.progress = 50 + (int)((double)i / assets.size() * 49);
            }
            this.progress = 99;
        } else {
        	throw new IOException(chooseFromSide("Client", "Server") + " sources not found!");
        }
    }

	public void recompile(JavaCompiler compiler, DiagnosticCollector<JavaFileObject> ds, Iterable<File> src, Iterable<String> recompileOptions) throws IOException, RuntimeException {
        StandardJavaFileManager mgr = compiler.getStandardFileManager(ds, null, null);
        Iterable<? extends JavaFileObject> sources = mgr.getJavaFileObjectsFromFiles(src);
        JavaCompiler.CompilationTask task = compiler.getTask(null, mgr, ds, recompileOptions, null, sources);
        mgr.close();
        boolean success = task.call();
        for (Diagnostic<? extends JavaFileObject> diagnostic : ds.getDiagnostics())
        	if(diagnostic.getKind() == Diagnostic.Kind.ERROR || diagnostic.getKind() == Diagnostic.Kind.WARNING) {
        		String kind = diagnostic.getKind() == Diagnostic.Kind.ERROR ? "Error" : "Warning";
            	info.addInfo(kind + String.format(" on line %d in %s%n%s%n",
                                  		diagnostic.getLineNumber(),
                                  		diagnostic.getSource().getName(),
                                  		diagnostic.getMessage(null)));
        	}
        if (!success) {
            throw new RuntimeException("Compilation error!");
        }
    }

    @Override
    public ProgressInfo getProgress() {
        if(step == RECOMPILE) {
        	return new ProgressInfo("Recompiling...", progress, total);
        }
        if(step == COPYRES) {
        	return new ProgressInfo("Copying resources...", progress, total);
        }
        return super.getProgress();
    }
}
