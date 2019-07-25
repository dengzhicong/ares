package com.ares.scripts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.xml.ws.spi.http.HttpHandler;

import com.ares.file.FileUtil;
import com.ares.handler.HandlerEntity;
import com.ares.handler.HttpHandlerEntity;
import com.ares.handler.IHandler;
import com.ares.handler.TcpHandler;
import com.ares.log.LogUtil;

/**
 * 脚本加载管理容器
 *
 * @author JiangZhiYong
 * @date 2017-03-30 
 */
public final class ScriptPool {

	// 源文件夹
	private String sourceDir;
	// 输出文件夹
	private String outDir;
	// 附加的jar包地址
	private String jarsDir;

	// 脚本容器
	Map<String, Map<String, IScript>> scriptInstances = new ConcurrentHashMap<>(0);
	Map<String, Map<String, IScript>> tmpScriptInstances = new ConcurrentHashMap<>(0);
	
	// tcp handler容器
	Map<Integer, Class<? extends IHandler>> tcpHandlerMap = new ConcurrentHashMap<>(0);
	Map<Integer, HandlerEntity> tcpHandlerEntityMap = new ConcurrentHashMap<>(0);

	// http handler容器
	Map<String, Class<? extends HttpHandler>> httpHandlerMap = new ConcurrentHashMap<>(0);
	Map<String, HttpHandlerEntity> httpHandlerEntityMap = new ConcurrentHashMap<>(0);

	public ScriptPool() {

	}

	/**
	 * 设置编译脚本属性
	 * 
	 * @param source
	 *            Java 脚本路径
	 * @param out
	 *            class编译类路径
	 * @param jarsDir
	 *            依赖jar包路径
	 * @throws Exception
	 */
	public void setSource(String source, String out, String jarsDir) throws Exception {
		if (stringIsNullEmpty(source)) {
			LogUtil.error("指定 输入 输出 目录为空");
			throw new Exception("目录为空");
		}
		this.sourceDir = source;
		this.outDir = out;
		this.jarsDir = jarsDir;
		LogUtil.info("脚本指定 输入 " + source + " 输出 " + out + " jars目录 " + jarsDir);
	}


	/**
	 * 脚本列表
	 * 
	 * @param name
	 *            脚本名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> Collection<E> getEvts(String name) {
		Map<String, IScript> scripts = ScriptPool.this.scriptInstances.get(name);
		if (scripts != null) {
			return (Collection<E>) scripts.values();
		}
		return new ArrayList<>();
	}

	/**
	 * 脚本列表
	 * 
	 * @param clazz
	 *            脚本类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> Collection<E> getEvts(Class<E> clazz) {
		Map<String, IScript> scripts = ScriptPool.this.scriptInstances.get(clazz.getName());
		if (scripts != null) {
			return (Collection<E>) scripts.values();
		}
		return new ArrayList<>();
	}

	/**
	 * 执行脚本
	 *
	 * @param <T>
	 * @param scriptClass
	 *            脚本类
	 * @param action
	 *            调用的方法
	 */
	@SuppressWarnings("unchecked")
	public <T extends IScript> void executeScripts(Class<T> scriptClass, Consumer<T> action) {
		Collection<IScript> evts = getEvts(scriptClass.getName());
		if (evts != null && !evts.isEmpty() && action != null) {
			evts.forEach(scrpit -> {
				try {
					action.accept((T) scrpit);
				} catch (Exception e) {
					LogUtil.error("执行 IScript:" + scriptClass.getName(), e);
				}
			});
		}
	}

	/**
	 * 执行脚本，当执行结果为true时，中断执行，并返回true。否则统一返回执行false
	 *
	 * @param <T>
	 * @param scriptClass
	 *            脚本类
	 * @param condition
	 *            执行的方法
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends IScript> boolean predicateScripts(Class<? extends IScript> scriptClass, Predicate<T> condition) {
		Collection<IScript> evts = getEvts(scriptClass.getName());
		if (evts != null && !evts.isEmpty() && condition != null) {
			Iterator<IScript> iterator = evts.iterator();
			while (iterator.hasNext()) {
				try {
					if (condition.test((T) iterator.next())) {
						return true;
					}
				} catch (Exception e) {
					LogUtil.error("predicateScripts IScript:" + scriptClass.getName(), e);
				}
			}
		}
		return false;
	}

	/**
	 * 执行脚本，并返回一个值
	 * 
	 * @param scriptClass
	 *            脚本类
	 * @param function
	 *            执行的方法
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends IScript, R> R functionScripts(Class<? extends IScript> scriptClass, Function<T, R> function) {
		Collection<IScript> evts = getEvts(scriptClass.getName());
		if (evts != null && !evts.isEmpty() && function != null) {
			Iterator<IScript> iterator = evts.iterator();
			while (iterator.hasNext()) {
				try {
					R r = function.apply((T) iterator.next());
					if (r != null) {
						return r;
					}
				} catch (Exception e) {
					LogUtil.error("functionScripts IBaseScript:" + scriptClass.getName(), e);
				}
			}
		}
		return null;
	}

	/**
	 * 字符串为空字符串
	 * 
	 * @param str
	 * @return
	 */
	final boolean stringIsNullEmpty(String str) {
		return str == null || str.length() <= 0 || "".equals(str.trim());
	}

	// <editor-fold desc="public final void Compile()">
	/**
	 * 编译 java 源文件
	 *
	 * @return
	 */
	String compile() {
		FileUtil.deleteDirectory(this.outDir); // 删除之前的class文件
		List<File> sourceFileList = new ArrayList<>();
		FileUtil.getFiles(this.sourceDir, sourceFileList, ".java", null); // 获取源文件
		return this.compile(sourceFileList);
	}
	// </editor-fold>

	// <editor-fold desc="public final void Compile(String... fileNames)">
	/**
	 * 编译文件
	 *
	 * @param fileNames
	 *            文件列表
	 * @return 编译错误信息
	 */
	String compile(List<File> sourceFileList) {
		StringBuilder sb = new StringBuilder();
		if (null != sourceFileList) {
			DiagnosticCollector<JavaFileObject> oDiagnosticCollector = new DiagnosticCollector<>();
			// 获取编译器实例
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			// 获取标准文件管理器实例
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(oDiagnosticCollector, null,
					Charset.forName("utf-8"));
			try {
				// 没有java文件，直接返回
				if (sourceFileList.isEmpty()) {
					// log.warn(this.sourceDir + "目录下查找不到任何java文件");
					return this.sourceDir + "目录下查找不到任何java文件";
				}
				LogUtil.warn("找到脚本并且需要编译的文件共：" + sourceFileList.size());
				// 创建输出目录，如果不存在的话
				new java.io.File(this.outDir).mkdirs();
				// 获取要编译的编译单元
				Iterable<? extends JavaFileObject> compilationUnits = fileManager
						.getJavaFileObjectsFromFiles(sourceFileList);
				/**
				 * 编译选项，在编译java文件时，编译程序会自动的去寻找java文件引用的其他的java源文件或者class。
				 * -sourcepath选项就是定义java源文件的查找目录， -classpath选项就是定义class文件的查找目录。
				 */
				ArrayList<String> options = new ArrayList<>(0);
				options.add("-g");
				options.add("-source");
				options.add("1.8");
				// options.add("-Xlint");
				// options.add("unchecked");
				options.add("-encoding");
				options.add("UTF-8");
				options.add("-sourcepath");
				options.add(this.sourceDir); // 指定文件目录
				options.add("-d");
				options.add(this.outDir); // 指定输出目录

				ArrayList<File> jarsList = new ArrayList<>();
				FileUtil.getFiles(this.jarsDir, jarsList, ".jar", null);
				String jarString = "";
				jarString = jarsList.stream().map((jar) -> jar.getPath() + File.pathSeparator).reduce(jarString,
						String::concat);
				// log.warn("jarString:" + jarString);
				if (!stringIsNullEmpty(jarString)) {
					options.add("-classpath");
					options.add(jarString);// 指定附加的jar包
				}

				JavaCompiler.CompilationTask compilationTask = compiler.getTask(null, fileManager, oDiagnosticCollector,
						options, null, compilationUnits);
				// 运行编译任务
				Boolean call = compilationTask.call();
				if (!call) {
					oDiagnosticCollector.getDiagnostics().forEach(f -> {
						sb.append(";").append(((JavaFileObject) (f.getSource())).getName()).append(" line:")
								.append(f.getLineNumber());
						LogUtil.warn("加载脚本错误：" + ((JavaFileObject) (f.getSource())).getName() + " line:"
								+ f.getLineNumber());
					});
				}
			} catch (Exception ex) {
				sb.append(this.sourceDir).append("错误：").append(ex);
				LogUtil.error("加载脚本错误：", ex);
			} finally {
				try {
					fileManager.close();
				} catch (IOException ex) {
					LogUtil.error("", ex);
				}
			}
		} else {
			LogUtil.warn(this.sourceDir + "目录下查找不到任何java文件");
		}
		return sb.toString();
	}

	// </editor-fold>

	// <editor-fold desc="加载脚本 public void loadJava()">
	/**
	 * 加载脚本文件
	 *
	 * @param condition
	 *            输出条件
	 * @return
	 */
	public String loadJava(Consumer<String> condition) {
		String compile = this.compile();
		StringBuilder sb = new StringBuilder();
		if (compile == null || compile.isEmpty()) {
			List<File> sourceFileList = new ArrayList<>(0);
			// 得到编译后的class文件
			FileUtil.getFiles(this.outDir, sourceFileList, ".class", null);
			String[] fileNames = new String[sourceFileList.size()]; // 类路径列表
			for (int i = 0; i < sourceFileList.size(); i++) {
				fileNames[i] = sourceFileList.get(i).getPath();
				sb.append(fileNames[i]).append(";");
			}
			tmpScriptInstances = new ConcurrentHashMap<>();
			loadClass(fileNames);
			if (tmpScriptInstances.size() > 0) {
				scriptInstances.clear();
				scriptInstances = tmpScriptInstances;
			}
		} else {
			if (!compile.isEmpty()) {
				if (condition != null) {
					condition.accept(compile);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 加载脚本文件
	 *
	 * @param source
	 *            加载的文件或者目录
	 * @return
	 */
	public String loadJava(String... source) {
		FileUtil.deleteDirectory(this.outDir);
		List<File> sourceFileList = new ArrayList<>();
		FileUtil.getFiles(this.sourceDir, sourceFileList, ".java", fileAbsolutePath -> {
			if (source == null) {
				return true;
			}
			for (String str : source) {
				if (fileAbsolutePath.contains(str) || str.equals("")) {
					return true;
				}
			}
			return false;
		});
		String result = this.compile(sourceFileList);
		StringBuilder loadJava = new StringBuilder();
		if (result == null || result.isEmpty()) {
			sourceFileList.clear();
			FileUtil.getFiles(this.outDir, sourceFileList, ".class", fileAbsolutePath -> {
				if (source == null) {
					return true;
				}
				for (String str : source) {
					if (fileAbsolutePath.contains(str) || str.equals("")) {
						return true;
					}
				}
				return false;
			});
			String[] fileNames = new String[sourceFileList.size()];
			for (int i = 0; i < sourceFileList.size(); i++) {
				fileNames[i] = sourceFileList.get(i).getPath();
				loadJava.append(fileNames[i]).append(";");
			}
			tmpScriptInstances = new ConcurrentHashMap<>();
			loadClass(fileNames);
			if (tmpScriptInstances.size() > 0) {
				tmpScriptInstances.entrySet().stream().forEach((entry) -> {
					String key = entry.getKey();
					Map<String, IScript> value = entry.getValue();
					scriptInstances.put(key, value);
				});
			}
		}
		return loadJava.toString();
	}

	/**
	 * 加载脚本文件
	 *
	 * @param names
	 */
	void loadClass(String... names) {
		try {
			ScriptClassLoader loader = new ScriptClassLoader();
			for (String name : names) {
				String tmpName = name.replace(outDir, "").replace(".class", "").replace(File.separatorChar, '.');
				loader.loadClass(tmpName);
			}
		} catch (ClassNotFoundException e) {
			LogUtil.error("", e);
		}
	}
	// </editor-fold>

	// <editor-fold desc="自定义文件加载器 class ScriptClassLoader extends ClassLoader">
	class ScriptClassLoader extends ClassLoader {

		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			Class<?> defineClass = null;
			defineClass = super.loadClass(name);
			return defineClass;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Class<?> findClass(String name) {
			byte[] classData = getClassData(name);
			Class<?> defineClass = null;
			if (classData != null) {
				try {
					defineClass = defineClass(name, classData, 0, classData.length);
					String nameString = defineClass.getName();
					if (!Modifier.isAbstract(defineClass.getModifiers())
							&& !Modifier.isPrivate(defineClass.getModifiers())
							&& !Modifier.isStatic(defineClass.getModifiers()) && !nameString.contains("$")) {
						Object newInstance = defineClass.newInstance();
						List<Class<?>> interfaces = new ArrayList<>(); // 实现的接口
						if (IInitScript.class.isAssignableFrom(defineClass)
								|| IScript.class.isAssignableFrom(defineClass)) {
							Class<?> cls = defineClass;
							while (cls != null && !cls.isInterface() && !cls.isPrimitive()) {
								interfaces.addAll(Arrays.asList(cls.getInterfaces()));
								cls = cls.getSuperclass();
							}
							if (newInstance instanceof IInitScript) { // 执行初始方法
								((IInitScript) newInstance).init();
							}
						}
						
						// 脚本
						if (newInstance != null && !interfaces.isEmpty()) {
							for (Class<?> aInterface : interfaces) {
								if (IScript.class.isAssignableFrom(aInterface)) {
									if (!tmpScriptInstances.containsKey(aInterface.getName())) {
										tmpScriptInstances.put(aInterface.getName(), new ConcurrentHashMap<>());
									}
									LogUtil.info("脚本[" + nameString + "]加载到IScript容器");
									tmpScriptInstances.get(aInterface.getName()).put(defineClass.getName(),
											(IScript) newInstance);
								}
							}
						}

						// tcp handler
						if (IHandler.class.isAssignableFrom(defineClass)) {
							HandlerEntity handlerEntity = defineClass.getAnnotation(HandlerEntity.class);
							if (handlerEntity != null) {
								if (TcpHandler.class.isAssignableFrom(defineClass)) {
									tcpHandlerMap.put(handlerEntity.mid(), (Class<? extends IHandler>) defineClass);
									tcpHandlerEntityMap.put(handlerEntity.mid(), handlerEntity);
									LogUtil.info("[" + nameString + "]加载到tcp handler容器");
								}
							} else {
								LogUtil.warn("handler[" + defineClass.getSimpleName() + "]未添加注解");
							}
							// http handler
						} else if (HttpHandler.class.isAssignableFrom(defineClass)) {
							HttpHandlerEntity handlerEntity = defineClass.getAnnotation(HttpHandlerEntity.class);
							if (handlerEntity != null) {
								if (HttpHandler.class.isAssignableFrom(defineClass)) {
									httpHandlerMap.put(handlerEntity.rootPath(),
											(Class<? extends HttpHandler>) defineClass);
									httpHandlerEntityMap.put(handlerEntity.rootPath(), handlerEntity);
									LogUtil.info("[" + nameString + "]加载到http handler容器");
								} else {
									LogUtil.warn("handler[" + defineClass.getSimpleName() + "]未继承httpHandler");
								}
							} else {
								LogUtil.warn("handler[" + defineClass.getSimpleName() + "]未添加注解");
							}
						}

					} else {
						LogUtil.warn("没有加载脚本：" + nameString);
					}
				} catch (Exception ex) {
					LogUtil.error("加载脚本发生错误", ex);
				}
			}
			return defineClass;
		}

		private byte[] getClassData(String className) {
			String path = classNameToPath(className);
			// log.warn("加载脚本路径", path);
			InputStream ins = null;
			try {
				File file = new File(path);
				if (file.exists()) {
					ins = new FileInputStream(path);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int bufferSize = 4096;
					byte[] buffer = new byte[bufferSize];
					int bytesNumRead = 0;
					while ((bytesNumRead = ins.read(buffer)) != -1) {
						baos.write(buffer, 0, bytesNumRead);
					}
					return baos.toByteArray();
				} else {
					LogUtil.warn("自定义脚本文件不存在：" + path);
				}
			} catch (IOException e) {
				LogUtil.error("", e);
			} finally {
				if (ins != null) {
					try {
						ins.close();
					} catch (Exception e) {
						LogUtil.error("", e);
					}
				}
			}
			return null;
		}

		private String classNameToPath(String className) {
			File file = null;
			try {
				String path = outDir + className.replace('.', File.separatorChar) + ".class";
				// log.warn("classNameToPath path:{}", path);
				file = new File(path);
				if (!file.exists()) {
					LogUtil.warn("classNameToPath path:" + path + "不存在");
				}
				return file.getPath();
			} catch (Exception e) {
				LogUtil.error(outDir, e);
			}
			return "";
		}
	}
	// </editor-fold>

	public Map<Integer, Class<? extends IHandler>> getHandlerMap() {
		return tcpHandlerMap;
	}

	public Map<Integer, HandlerEntity> getHandlerEntityMap() {
		return tcpHandlerEntityMap;
	}

	public Map<String, Class<? extends HttpHandler>> getHttpHandlerMap() {
		return httpHandlerMap;
	}

	public Map<String, HttpHandlerEntity> getHttpHandlerEntityMap() {
		return httpHandlerEntityMap;
	}

	/**
	 * 添加handler
	 * 
	 * @author JiangZhiYong
	 * @QQ 359135103 2017年7月24日 下午1:36:27
	 * @param clazz
	 */
	public void addTcpHandler(Class<? extends IHandler> clazz) {
		if (IHandler.class.isAssignableFrom(clazz)) {
			HandlerEntity handlerEntity = clazz.getAnnotation(HandlerEntity.class);
			if (handlerEntity != null) {
				if (TcpHandler.class.isAssignableFrom(clazz)) {
					tcpHandlerMap.put(handlerEntity.mid(), (Class<? extends IHandler>) clazz);
					tcpHandlerEntityMap.put(handlerEntity.mid(), handlerEntity);
					LogUtil.info("[" + clazz.getName() + "]加载到tcp handler容器");
				} else {
					LogUtil.warn("handler[" + clazz.getSimpleName() + "]未继承Handler");
				}
			} else {
				LogUtil.warn("handler[" + clazz.getSimpleName() + "]未添加注解");
			}
		}
	}

	
	/**
	 * 添加httpHandler脚本
	 * 
	 * @param clazz
	 */
	public void addHttpHandler(Class<? extends HttpHandler> clazz) {
		if (HttpHandler.class.isAssignableFrom(clazz)) {
			HttpHandlerEntity handlerEntity = clazz.getAnnotation(HttpHandlerEntity.class);
			if (handlerEntity != null) {
				httpHandlerMap.put(handlerEntity.rootPath(), (Class<? extends HttpHandler>) clazz);
				httpHandlerEntityMap.put(handlerEntity.rootPath(), handlerEntity);
				LogUtil.info("[" + clazz.getName() + "]加载到http handler容器");
			} else {
				LogUtil.warn("handler[" + clazz.getSimpleName() + "]未添加注解");
			}
		} else {
			LogUtil.warn("handler[" + clazz.getSimpleName() + "]未继承 httpHandler");
		}
	}
}
