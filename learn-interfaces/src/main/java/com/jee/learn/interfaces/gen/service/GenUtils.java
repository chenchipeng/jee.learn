package com.jee.learn.interfaces.gen.service;




public class GenUtils {

	/*
	
	
	
	public static void initColumnField(GenTable genTable) {
		for (GenTableColumn column : genTable.getColumnList()) {

			if (!StringUtils.isNotBlank(column.getId())) {

				if (StringUtils.isBlank(column.getComments())) {
					column.setComments(column.getName());
				}

				if ((StringUtils.startsWithIgnoreCase(column.getJdbcType(), "CHAR"))
						|| (StringUtils.startsWithIgnoreCase(column.getJdbcType(), "VARCHAR"))
						|| (StringUtils.startsWithIgnoreCase(column.getJdbcType(), "NARCHAR"))) {
					column.setJavaType("String");
				} else if ((StringUtils.startsWithIgnoreCase(column.getJdbcType(), "DATETIME"))
						|| (StringUtils.startsWithIgnoreCase(column.getJdbcType(), "DATE"))
						|| (StringUtils.startsWithIgnoreCase(column.getJdbcType(), "TIMESTAMP"))) {
					column.setJavaType("java.util.Date");
					column.setShowType("dateselect");
				} else if ((StringUtils.startsWithIgnoreCase(column.getJdbcType(), "BIGINT"))
						|| (StringUtils.startsWithIgnoreCase(column.getJdbcType(), "NUMBER"))) {
					String[] ss = StringUtils.split(StringUtils.substringBetween(column.getJdbcType(), "(", ")"), ",");
					if ((ss != null) && (ss.length == 2) && (Integer.parseInt(ss[1]) > 0)) {
						column.setJavaType("Double");

					} else if ((ss != null) && (ss.length == 1) && (Integer.parseInt(ss[0]) <= 10)) {
						column.setJavaType("Integer");
					} else {
						column.setJavaType("Long");
					}
				} else if (StringUtils.startsWithIgnoreCase(column.getJdbcType(), "DECIMAL")) {
					column.setJavaType("java.math.BigDecimal");
				} else if (StringUtils.startsWithIgnoreCase(column.getJdbcType(), "INT")) {
					column.setJavaType("Integer");
				}

				column.setJavaField(ChnskinStringUtils.toCamelCase(column.getName()));

				column.setIsPk(genTable.getPkList().contains(column.getName()) ? "1" : "0");

				column.setIsInsert("1");

				if ((!StringUtils.equalsIgnoreCase(column.getName(), "id"))
						&& (!StringUtils.equalsIgnoreCase(column.getName(), "create_by"))
						&& (!StringUtils.equalsIgnoreCase(column.getName(), "create_date"))
						&& (!StringUtils.equalsIgnoreCase(column.getName(), "del_flag"))
						&& (!StringUtils.equalsIgnoreCase(column.getName(), "account_id"))) {
					column.setIsEdit("1");
				}

				if ((StringUtils.equalsIgnoreCase(column.getName(), "name"))
						|| (StringUtils.equalsIgnoreCase(column.getName(), "title"))
						|| (StringUtils.equalsIgnoreCase(column.getName(), "remarks"))
						|| (StringUtils.equalsIgnoreCase(column.getName(), "update_date"))) {
					column.setIsList("1");
				}

				if ((StringUtils.equalsIgnoreCase(column.getName(), "name"))
						|| (StringUtils.equalsIgnoreCase(column.getName(), "title"))) {
					column.setIsQuery("1");
				}

				if ((StringUtils.equalsIgnoreCase(column.getName(), "name"))
						|| (StringUtils.equalsIgnoreCase(column.getName(), "title"))) {
					column.setQueryType("like");
				}
				
				
				
				//------------------------------------
				
				
				
				if ((StringUtils.equalsIgnoreCase(column.getName(), "sort"))) {
					column.setJavaType("Integer");
				}

				if (StringUtils.startsWithIgnoreCase(column.getName(), "user_id")) {
					column.setJavaType(SysUser.class.getName());
					column.setJavaField(column.getJavaField().replaceAll("Id", ".name"));
					column.setShowType("userselect");

				} else if (StringUtils.startsWithIgnoreCase(column.getName(), "office_id")) {
					column.setJavaType(SysOffice.class.getName());
					column.setJavaField(column.getJavaField().replaceAll("Id", ".name"));
					column.setShowType("officeselect");

				} else if (StringUtils.startsWithIgnoreCase(column.getName(), "area_id")) {
					column.setJavaType(Area.class.getName());
					column.setJavaField(column.getJavaField().replaceAll("Id", ".name"));
					column.setShowType("areaselect");

				} else if ((StringUtils.startsWithIgnoreCase(column.getName(), "create_by"))
						|| (StringUtils.startsWithIgnoreCase(column.getName(), "update_by"))
						|| (StringUtils.startsWithIgnoreCase(column.getName(), "audit_by"))
						|| (StringUtils.startsWithIgnoreCase(column.getName(), "deal_by"))) {
					column.setJavaType(SysUser.class.getName());
					column.setJavaField(column.getJavaField() + ".name");

				} else if ((StringUtils.startsWithIgnoreCase(column.getName(), "create_date"))
						|| (StringUtils.startsWithIgnoreCase(column.getName(), "update_date"))
						|| (StringUtils.startsWithIgnoreCase(column.getName(), "audit_date"))
						|| (StringUtils.startsWithIgnoreCase(column.getName(), "deal_date"))) {
					column.setShowType("dateselect");

				} else if ((StringUtils.equalsIgnoreCase(column.getName(), "remarks"))
						|| (StringUtils.equalsIgnoreCase(column.getName(), "content"))) {
					column.setShowType("textarea");

				} else if (StringUtils.equalsIgnoreCase(column.getName(), "parent_id")) {
					column.setJavaType("This");
					column.setJavaField("parent.id|name");
					column.setShowType("treeselect");

				} else if (StringUtils.equalsIgnoreCase(column.getName(), "parent_ids")) {
					column.setQueryType("like");

				} else if (StringUtils.equalsIgnoreCase(column.getName(), "del_flag")) {
					column.setShowType("radiobox");
					column.setDictType("del_flag");
				}
			}
		}
	}

	public static String getTemplatePath() {
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null) {
				return file.getAbsolutePath() + File.separator
						+ StringUtils.replaceEach(GenUtils.class.getName(),
								new String[] { "util." + GenUtils.class.getSimpleName(), "." },
								new String[] { "template", File.separator });
			}
		} catch (Exception e) {
			logger.error("{}", e);
		}

		return "";
	}

	@SuppressWarnings("unchecked")
	public static <T> T fileToObject(String fileName, Class<?> clazz) {
		try {
			String pathName = "/templates/" + fileName;

			Resource resource = new ClassPathResource(pathName);
			String content = FileUtils.readFileToString(resource.getFile(), "UTF-8");

			return (T) JaxbMapper.fromXml(content, clazz);
		} catch (IOException e) {
			logger.warn("Error file convert: {}", e);
		}

		return null;
	}

	public static GenConfig getConfig() {
		return (GenConfig) fileToObject("config.xml", GenConfig.class);
	}

	public static List<GenTemplate> getTemplateList(GenConfig config, String category, boolean isChildTable) {
		List<GenTemplate> templateList = Lists.newArrayList();
		if ((config != null) && (config.getCategoryList() != null) && (category != null)) {
			for (GenCategory e : config.getCategoryList()) {
				if (category.equals(e.getValue())) {
					List<String> list = null;
					if (!isChildTable) {
						list = e.getTemplate();
					} else {
						list = e.getChildTableTemplate();
					}
					if (list == null)
						break;
					for (String s : list) {
						if (StringUtils.startsWith(s, GenCategory.CATEGORY_REF)) {
							templateList.addAll(getTemplateList(config,
									StringUtils.replace(s, GenCategory.CATEGORY_REF, ""), false));
						} else {
							GenTemplate template = (GenTemplate) fileToObject(s, GenTemplate.class);
							if (template != null) {
								templateList.add(template);
							}
						}
					}

					break;
				}
			}
		}
		return templateList;
	}

	public static Map<String, Object> getDataModel(GenScheme genScheme) {
		Map<String, Object> model = Maps.newHashMap();

		model.put("packageName", StringUtils.lowerCase(genScheme.getPackageName()));
		model.put("lastPackageName", StringUtils.substringAfterLast((String) model.get("packageName"), "."));
		model.put("moduleName", StringUtils.lowerCase(genScheme.getModuleName()));
		model.put("subModuleName", StringUtils.lowerCase(genScheme.getSubModuleName()));
		model.put("className", StringUtils.uncapitalize(genScheme.getGenTable().getClassName()));
		model.put("ClassName", StringUtils.capitalize(genScheme.getGenTable().getClassName()));

		model.put("functionName", genScheme.getFunctionName());
		model.put("functionNameSimple", genScheme.getFunctionNameSimple());
		model.put("functionAuthor", StringUtils.isNotBlank(genScheme.getFunctionAuthor())
				? genScheme.getFunctionAuthor() : UserUtils.getUser().getName());
		model.put("functionVersion", ChnskinDateUtils.getDate());

		model.put("urlPrefix",
				model.get("moduleName")
						+ (StringUtils.isNotBlank(genScheme.getSubModuleName())
								? "/" + StringUtils.lowerCase(genScheme.getSubModuleName()) : "")
						+ "/" + model.get("className"));
		model.put("viewPrefix", model.get("urlPrefix"));
		model.put("permissionPrefix",
				model.get("moduleName")
						+ (StringUtils.isNotBlank(genScheme.getSubModuleName())
								? ":" + StringUtils.lowerCase(genScheme.getSubModuleName()) : "")
						+ ":" + model.get("className"));

		model.put("dbType", Global.getConfig("jdbc.type"));
		model.put("isExport", genScheme.getIsExport());

		model.put("table", genScheme.getGenTable());

		return model;
	}

	public static String generateToFile(GenTemplate tpl, Map<String, Object> model, boolean isReplaceFile) {
		String fileName = Global.getProjectPath() + File.separator + StringUtils.replaceEach(
				FreeMarkers.renderString(new StringBuilder(String.valueOf(tpl.getFilePath())).append("/").toString(),
						model),
				new String[] { "//", "/", "." }, new String[] { File.separator, File.separator, File.separator })
				+ FreeMarkers.renderString(tpl.getFileName(), model);
		logger.debug(" fileName === {}", fileName);

		String content = FreeMarkers.renderString(StringUtils.trimToEmpty(tpl.getContent()), model);

		if (isReplaceFile) {
			ChnskinFileUtils.deleteFile(fileName);
		}

		if (ChnskinFileUtils.createFile(fileName)) {
			ChnskinFileUtils.writeToFile(fileName, content, true);
			return "生成成功：" + fileName + "<br/>";
		}
		return "文件已存在：" + fileName + "<br/>";
	}

	public static void main(String[] args) {
		try {
			GenConfig config = getConfig();
			System.out.println(config);
			System.out.println(JaxbMapper.toXml(config));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	*/
}
