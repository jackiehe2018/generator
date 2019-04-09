/**
 *    Copyright 2006-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.internal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

/**
 *  MybatisCommentGenerator for <commentGenerator  type="org.mybatis.generator.internal.MybatisCommentGenerator"></commentGenerator>
 *  if you use this code ,please add <property name="addRemarkComments" value="true"/>
 *  this Generator is only on generator-jackie
 * @author Jackie
 * @date 2019-04-09 16:12
 * @version 1.3.5 
 */
public class MybatisCommentGenerator implements CommentGenerator {
	private Properties properties = new Properties();
	private boolean suppressDate = false;
	private boolean suppressAllComments = false;
	private boolean addRemarkComments = false;
	private SimpleDateFormat dateFormat;

	public MybatisCommentGenerator() {
	}

	@Override
	public void addJavaFileComment(CompilationUnit compilationUnit) {
	}

	@Override
	public void addComment(XmlElement xmlElement) {
	}

	@Override
	public void addRootComment(XmlElement rootElement) {
	}

	@Override
	public void addConfigurationProperties(Properties properties) {
		this.properties.putAll(properties);
		this.suppressDate = StringUtility.isTrue(properties.getProperty("suppressDate"));
		this.suppressAllComments = StringUtility.isTrue(properties.getProperty("suppressAllComments"));
		this.addRemarkComments = StringUtility.isTrue(properties.getProperty("addRemarkComments"));
		String dateFormatString = properties.getProperty("dateFormat");
		if (StringUtility.stringHasValue(dateFormatString)) {
			this.dateFormat = new SimpleDateFormat(dateFormatString);
		}

	}

	private void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
	}

	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
		if (!this.suppressAllComments) {
			StringBuilder sb = new StringBuilder();
			innerClass.addJavaDocLine("/**");
			sb.append(" * ");
			sb.append(introspectedTable.getFullyQualifiedTable());
			innerClass.addJavaDocLine(sb.toString());
			this.addJavadocTag(innerClass, false);
			innerClass.addJavaDocLine(" */");

		}
	}

	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		if (!this.suppressAllComments) {
			StringBuilder sb = new StringBuilder();
			innerClass.addJavaDocLine("/**");
			sb.append(" * ");
			sb.append(introspectedTable.getFullyQualifiedTable());
			innerClass.addJavaDocLine(sb.toString());
			this.addJavadocTag(innerClass, markAsDoNotDelete);
			innerClass.addJavaDocLine(" */");
		}
	}

	@Override
	public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (!this.suppressAllComments && this.addRemarkComments) {
			topLevelClass.addJavaDocLine("import lombok.Data;");
			topLevelClass.addJavaDocLine("import lombok.NoArgsConstructor;");
			topLevelClass.addJavaDocLine("import io.swagger.annotations.ApiModel;");
			topLevelClass.addJavaDocLine("import io.swagger.annotations.ApiModelProperty;");
			topLevelClass.addJavaDocLine("import lombok.experimental.Accessors;\n");
			topLevelClass.addJavaDocLine("/**");
			String remarkLine = "";
			String remarks = introspectedTable.getRemarks();
			if (this.addRemarkComments && StringUtility.stringHasValue(remarks)) {
//				topLevelClass.addJavaDocLine(" * Database Table Remarks:");
				String[] remarkLines = remarks.split(System.getProperty("line.separator"));
				String[] var5 = remarkLines;
				int var6 = remarkLines.length;
				for(int var7 = 0; var7 < var6; ++var7) {
					remarkLine += var5[var7];
//					topLevelClass.addJavaDocLine(" *   " + remarkLine);
				}
			}
			StringBuilder sb = new StringBuilder();
			String name = String.valueOf(introspectedTable.getFullyQualifiedTable());
			sb.append(" * @className " + introspectedTable.getTableConfiguration().getDomainObjectName() + "\n");
			sb.append(" * @description " + remarkLine + " \n " + "* @author Jackie" + "\n");
			sb.append(" * @date " + new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date()) + "\n");
			sb.append(" * @version 1.0");
			topLevelClass.addJavaDocLine(sb.toString());
			this.addJavadocTag(topLevelClass, true);
			topLevelClass.addJavaDocLine(" */\n");
			topLevelClass.addJavaDocLine("@Data");
			topLevelClass.addJavaDocLine("@NoArgsConstructor");
			topLevelClass.addJavaDocLine("@Accessors(chain=true)");
			topLevelClass.addJavaDocLine("@ApiModel(\""+ remarkLine +"\")");
		}
	}

	@Override
	public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
		if (!this.suppressAllComments) {
			innerEnum.addJavaDocLine("/**");
			innerEnum.addJavaDocLine(" * ");
			innerEnum.addJavaDocLine(" */");
		}
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
		if (!this.suppressAllComments) {
			if (introspectedColumn.getRemarks() != null && !introspectedColumn.getRemarks().equals("")) {
				field.addJavaDocLine("/** ");
				field.addJavaDocLine("* " + introspectedColumn.getRemarks());
				field.addJavaDocLine("**/ ");
				field.addJavaDocLine( "@ApiModelProperty(\""+ introspectedColumn.getRemarks() + "\")");
				addJavadocTag(field, false);
			}
		}
	}

	/**
	 * 设置序列化ID的注释
	 * @param field
	 * @param introspectedTable
	 */
	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		if (!this.suppressAllComments) {
			field.addJavaDocLine("/**");
			field.addJavaDocLine(" * Serializable ID");
			field.addJavaDocLine(" */");
		}
	}

	@Override
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
//		if (!this.suppressAllComments) {
//			method.addJavaDocLine("/**");
//			method.addJavaDocLine(" * @method " + method.getName());
//			for (Parameter param : method.getParameters()){
//				method.addJavaDocLine(" * @param " + param.getName());
//			}
//			method.addJavaDocLine(" */");
//		}
	}

	@Override
	public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
	}

	@Override
	public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

	}

	public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
		imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
		String comment = "Source Table: " + introspectedTable.getFullyQualifiedTable().toString();
		method.addAnnotation(this.getGeneratedAnnotation(comment));
	}

	public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
		imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
		String comment = "Source field: " + introspectedTable.getFullyQualifiedTable().toString() + "." + introspectedColumn.getActualColumnName();
		method.addAnnotation(this.getGeneratedAnnotation(comment));
	}

	public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
		imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
		String comment = "Source Table: " + introspectedTable.getFullyQualifiedTable().toString();
		field.addAnnotation(this.getGeneratedAnnotation(comment));
	}

	public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
		imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
		String comment = "Source field: " + introspectedTable.getFullyQualifiedTable().toString() + "." + introspectedColumn.getActualColumnName();
		field.addAnnotation(this.getGeneratedAnnotation(comment));
	}

	public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
		imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
		String comment = "Source Table: " + introspectedTable.getFullyQualifiedTable().toString();
		innerClass.addAnnotation(this.getGeneratedAnnotation(comment));
	}

	private String getGeneratedAnnotation(String comment) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("@Generated(");
		if (this.suppressAllComments) {
			buffer.append('"');
		} else {
			buffer.append("value=\"");
		}

		buffer.append(MyBatisGenerator.class.getName());
		buffer.append('"');
		if (!this.suppressDate && !this.suppressAllComments) {
			buffer.append(", date=\"");
			buffer.append(DatatypeConverter.printDateTime(Calendar.getInstance()));
			buffer.append('"');
		}

		if (!this.suppressAllComments) {
			buffer.append(", comments=\"");
			buffer.append(comment);
			buffer.append('"');
		}

		buffer.append(')');
		return buffer.toString();
	}
}
