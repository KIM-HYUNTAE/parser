package SW_visualization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class JavaParserPrac {
	public CompilationUnit compilationUnit;

	public JavaParserPrac() {
		String fileName = "./target/Sample.java";
		File ffile = new File(fileName);
		File[] fileArray = ffile.listFiles();

		if (ffile.listFiles() != null) {
			for (int i = 0; i < fileArray.length; i++) {
				fileName = fileArray[i].toString();
				if (!fileArray[i].isFile()) {
					System.out.println(fileArray[i].getAbsolutePath() + "does not exist.");
					System.exit(1);
				}
				System.out.println(fileArray[i] + " load complete!\n");
			}
			//System.out.println("digraph packageName{");
			for (int i = 0; i < fileArray.length; i++) {
				fileName = fileArray[i].toString();
				System.out.println("load: "+fileArray[i]);
				parse(fileArray[i]);
			}
			//System.out.println("}");
		} 
		else {
			System.out.println("load: "+fileName);
			parse(ffile);
		}
	}

	public void parse(File file) {
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		StringBuilder source = new StringBuilder();
		char[] buf = new char[50];
		int numRead = 0;

		try {
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				source.append(readData);
				buf = new char[1024];
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ���� �б� �� �ɼ� ���� ����
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(source.toString().toCharArray());
		parser.setStatementsRecovery(true);
		parser.setBindingsRecovery(true);
		parser.setResolveBindings(true);
		// �ɼǳ�
		Map<?, ?> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
		parser.setCompilerOptions(options);

		compilationUnit = (CompilationUnit) parser.createAST(null);	//AST ��ü ����
		// ���⼭���� �ڵ� ����
		
		startParser();
}
	public void startParser() {
		List<AbstractTypeDeclaration> types = compilationUnit.types();
		for (AbstractTypeDeclaration type : types) {
			if (type instanceof TypeDeclaration) {
				typeDeclaration((TypeDeclaration) type);
			}
		}
	}

	public void typeDeclaration(TypeDeclaration type) {
		String sintertype;
		//System.out.println(compilationUnit.types().get(0).getClass());
		TypeDeclaration typeDeclaration = (TypeDeclaration) compilationUnit.types().get(0);
		
		String name = type.getName().toString();
		System.out.println("\n=========================================");
		System.out.println("Ŭ���� �̸�: "+name);
		
		System.out.print("����ϴ� Ŭ����: ");
		try {
			String typesuperclass = type.getSuperclassType().toString();
			System.out.println(typesuperclass);
		}
		catch (NullPointerException e) {
			System.out.println("");
		}
		
		System.out.print("�����ϴ� �������̽�: ");
		List <SimpleType> typesuperinterface=type.superInterfaceTypes();
		for (SimpleType tsi:typesuperinterface) {
			if(typesuperinterface!=null) {
				System.out.print(typesuperinterface.get(0));
			}
			else {
				System.out.println("\n");
			}
		}
		
		/*try {
		String typesuperclass = type.getSuperclassType().toString();
		System.out.print(typeName + " [shape = \"record\", label = \"{ ");
	}

	catch (NullPointerException e) {
		System.out.print(typeName + " [shape = \"record\", label = \"{ ");
	}*/
	/*
	System.out.print("Abstract: ");
	List<Object> aaaaa = type.modifiers();
	int cnt = 0;
	for (Object a : aaaaa) {
		if (a.toString().equals("abstract")) {
			cnt++;
		}
	}
	if (cnt >= 1) {
		System.out.print("true");
		//System.out.print(" \\<Abstract\\> ");
	}
	System.out.print("\nInterface: ");
	if (type.isInterface() == true) {
		System.out.println("true");
		//System.out.print(" \\<Interface\\> ");
	}*/
		/*for (MethodDeclaration method : type.getMethods()) {
			methodDeclaration(method);
		}*/
		/*
		 * System.out.print("\n���� ���������� Ŭ����: ");
		 * for (FieldDeclaration field :type.getFields()) { 
		 * fieldDeclaration(field); System.out.print(" ");
		 * //System.out.println("\\l"); }
		 */
		/*
		 * System.out.print("\n���� ���������� Ŭ����: "); for (FieldDeclaration field :
		 * type.getFields()) { fieldDeclaration(field); System.out.print(" ");
		 * //System.out.println("\\l"); }
		 */
		/*
		 * System.out.print("\n���������� Ŭ����: "); for (MethodDeclaration method :
		 * type.getMethods()) { methodDeclaration(method); }
		 */
		//System.out.println("\n|");

		for (MethodDeclaration method : type.getMethods()) {
			methodDeclaration(method);
			System.out.print("\n");
			//System.out.println("\\l");
		}
		//System.out.print(" }\"]\n");

}
	
	public void fieldDeclaration(FieldDeclaration field) {
		// System.out.print("\t");
		List<Modifier> modifiers = field.modifiers();
		for (Modifier mo : modifiers) {
			modifiers(mo);
		}
		String field_name = field.fragments().get(0).toString().split("=")[0];
		char[] field_names = field_name.toCharArray();
		/*for (int i = 0; i < field_name.length(); i++) {
			if (field_name.charAt(i) == '<' || field_name.charAt(i) == '>') {
				System.out.print("\\");
			} else if (field_name.charAt(i) == '{' || field_name.charAt(i) == '}') {
				System.out.print("\\");
			} else if (field_name.charAt(i) == '"') {
				System.out.print("\\");
			}
			System.out.print(field_names[i]);
		}
		System.out.print(": ");*/
		// �ʵ� �ڷ���
		String field_type = field.getType().toString();
		System.out.print(field_type);
		/*char[] field_types = field_type.toCharArray();
		for (int i = 0; i < field_type.length(); i++) {
			if (field_type.charAt(i) == '<' || field_type.charAt(i) == '>') {
				System.out.print("\\");
			}
			//System.out.print(field_types[i]);
		}*/
	}

	public void methodDeclaration(MethodDeclaration method) {
		method.getBody().statements().getClass().toString();
		if (method.getBody()!=null) {
			String [] statements = method.getBody().toString().split("\n");
			for(String statement:statements) {
				boolean state = statement.contains("new ");
				if(state == true) {
					int index_start = statement.indexOf("new ")+4;
					int index_finish=statement.indexOf("(");
					StringBuilder a_class = new StringBuilder();
					
					for(int i = index_start; i< index_finish;i++) {
						a_class.append(statement.charAt(i));
					}
					String as_class = a_class.toString();
					
				}
			}
		}	
			
		for (int i = 0; i < method.modifiers().size(); i++) {
			if (method.modifiers().get(0) instanceof Modifier) {
				modifiers((Modifier) method.modifiers().get(i));
			}
		}
		System.out.print("");
		//System.out.print("(");
		/*
		 * if (method.parameters().isEmpty() != true) { List<SingleVariableDeclaration>
		 * ps = method.parameters(); int cnt = 0; for (SingleVariableDeclaration p : ps)
		 * { if (cnt > 0) { System.out.print(", "); } singleVariableDeclaration(p);
		 * cnt++; } }
		 */
		//System.out.print("): " + method.getReturnType2());
	}

	public void modifiers(Modifier mo) {
		String keyword = mo.getKeyword().toString();
		/*if (keyword.equals("private"))
			System.out.print("-");
		else if (keyword.equals("public"))
			System.out.print("+");
		else if (keyword.equals("protected"))
			System.out.print("#");*/
	}

	public void singleVariableDeclaration(SingleVariableDeclaration s) {
		//System.out.print(s.getName() + ":");
		String method_param1 = s.getType().toString();
		/*char[] method_params1 = method_param1.toCharArray();
		for (int i = 0; i < method_param1.length(); i++) {
			if (method_param1.charAt(i) == '<' || method_param1.charAt(i) == '>') {
				System.out.print("\\");
			} else if (method_param1.charAt(i) == '{' || method_param1.charAt(i) == '}') {
				System.out.print("\\");
			} else if (method_param1.charAt(i) == '"') {
				System.out.print("\\");
			}
		}*/
		System.out.print("!!!"+method_param1);
	}
	
}

/*
System.out.println(compilationUnit.types().get(0).getClass());
TypeDeclaration typeDeclaration=(TypeDeclaration)compilationUnit.types().get(0);

if(typeDeclaration.getSuperclassType()!=null) {
	System.out.println("����ϴ� Ŭ����: "+typeDeclaration.getSuperclassType());
}

List<SimpleType> td = typeDeclaration.superInterfaceTypes();
for (Object t:td) {
	if(typeDeclaration.superInterfaceTypes().size()!=0) {
		System.out.println("�����ϴ� �������̽� "+td);
	}
}

if (typeDeclaration.getClass().toString().equals("class org.eclipse.jdt.core.dom.TypeDeclaration")) {
	TypeDeclaration classInfo = (TypeDeclaration) typeDeclaration;
	
	//System.out.print(classInfo.getName() + " [shape = \"record\" label = \"{");
	
	List<Object> aaaa = classInfo.modifiers();
	int cnt = 0;
	for (Object a : aaaa) {
		if (a.toString().equals("abstract")) {
			cnt++;
		}
	}
	if (cnt >= 1) {
		System.out.println("\\<Abstract\\>");
	}
	if (classInfo.isInterface() == true)
		System.out.print("\\<Interface\\>");
	System.out.print(classInfo.getName().toString() + " |");

}
System.out.print("\n");

for (Object bodyDeclaration : typeDeclaration.bodyDeclarations()) {
	if (bodyDeclaration.getClass().toString().equals("class org.eclipse.jdt.core.dom.FieldDeclaration")) {
		FieldDeclaration field = (FieldDeclaration) bodyDeclaration;
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) field.fragments().get(0);

		if (field.modifiers().get(0).toString().equals("public"))
			System.out.print("+");
		else if (field.modifiers().get(0).toString().equals("private"))
			System.out.print("-");
		else if (field.modifiers().get(0).toString().equals("protected"))
			System.out.print("#");
		String fName = fragment.getName().getIdentifier();
		String fType = field.getType().toString();
		String FTYPE = fType.replace("<", "\\<").replace(">", "\\>");
		System.out.print(fName + ": " + FTYPE + "\\l \n");
	}
}

System.out.print("|");

for (Object bodyDeclaration : typeDeclaration.bodyDeclarations()) {
	if (bodyDeclaration.getClass().toString().equals("class org.eclipse.jdt.core.dom.MethodDeclaration")) {
		MethodDeclaration method = (MethodDeclaration) bodyDeclaration;
		List<SingleVariableDeclaration> ps = method.parameters();
		System.out.println();
		if (method.modifiers().get(0).toString().equals("public"))
			System.out.print("+");
		else if (method.modifiers().get(0).toString().equals("private"))
			System.out.print("-");
		else if (method.modifiers().get(0).toString().equals("protected"))
			System.out.print("#");
		String MName = method.getName().toString();
		System.out.print(MName + "(");

		for (Object parameter : method.parameters()) {
			System.out.print(parameter + ":");
		}
		for (SingleVariableDeclaration p : ps) {
			String Ptype = p.getType().toString();
			String PTYPE = Ptype.replace("<", "\\<").replace(">", "\\>");
			System.out.print(p.getName().toString() + ":" + PTYPE);
		}
		String m = method.getReturnType2().toString();
		String M = m.replace("<", "\\<").replace(">", "\\>");
		System.out.print("):" + M + "\\l");
	}
}
System.out.println("\"]}");
}
}*/





/*if (typeDeclaration.getClass().toString().equals("class org.eclipse.jdt.core.dom.TypeDeclaration")) {
	TypeDeclaration classInfo = (TypeDeclaration) typeDeclaration;
	System.out.print(classInfo.getName() + " [shape = \"record\" label = \"{");

	List<Object> aaaa = classInfo.modifiers();
	int cnt = 0;
	for (Object a : aaaa) {
		if (a.toString().equals("abstract")) {
			cnt++;
		}
	}
	if (cnt >= 1) {
		System.out.println("\\<Abstract\\>");
	}
	if (classInfo.isInterface() == true)
		System.out.print("\\<Interface\\>");
	System.out.print(classInfo.getName().toString() + " |");

}
System.out.print("\n");

for (Object bodyDeclaration : typeDeclaration.bodyDeclarations()) {
	if (bodyDeclaration.getClass().toString().equals("class org.eclipse.jdt.core.dom.FieldDeclaration")) {
		FieldDeclaration field = (FieldDeclaration) bodyDeclaration;
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) field.fragments().get(0);

		if (field.modifiers().get(0).toString().equals("public"))
			System.out.print("+");
		else if (field.modifiers().get(0).toString().equals("private"))
			System.out.print("-");
		else if (field.modifiers().get(0).toString().equals("protected"))
			System.out.print("#");
		String fName = fragment.getName().getIdentifier();
		String fType = field.getType().toString();
		String FTYPE = fType.replace("<", "\\<").replace(">", "\\>");
		System.out.print(fName + ": " + FTYPE + "\\l \n");
	}
}

System.out.print("|");

for (Object bodyDeclaration : typeDeclaration.bodyDeclarations()) {
	if (bodyDeclaration.getClass().toString().equals("class org.eclipse.jdt.core.dom.MethodDeclaration")) {
		MethodDeclaration method = (MethodDeclaration) bodyDeclaration;
		List<SingleVariableDeclaration> ps = method.parameters();
		System.out.println();
		if (method.modifiers().get(0).toString().equals("public"))
			System.out.print("+");
		else if (method.modifiers().get(0).toString().equals("private"))
			System.out.print("-");
		else if (method.modifiers().get(0).toString().equals("protected"))
			System.out.print("#");
		String MName = method.getName().toString();
		System.out.print(MName + "(");

		for (Object parameter : method.parameters()) {
			System.out.print(parameter + ":");
		}
		for (SingleVariableDeclaration p : ps) {
			String Ptype = p.getType().toString();
			String PTYPE = Ptype.replace("<", "\\<").replace(">", "\\>");
			System.out.print(p.getName().toString() + ":" + PTYPE);
		}
		String m = method.getReturnType2().toString();
		String M = m.replace("<", "\\<").replace(">", "\\>");
		System.out.print("):" + M + "\\l");
	}
}
System.out.println("\"]}");
}
}*/
/*
public void startParser() {
	List<AbstractTypeDeclaration> types = compilationUnit.types();
	for (AbstractTypeDeclaration type : types) {
		if (type instanceof TypeDeclaration) {
			typeDeclaration((TypeDeclaration) type);
		}
	}
}

public void typeDeclaration(TypeDeclaration type) {
	String typeName = type.getName().toString();
	String sintertype;
	List<SimpleType> aaaa = type.superInterfaceTypes();
	for (SimpleType a : aaaa) {
		if (type.superInterfaceTypes().size() > 0) {
			// System.out.println(typeName+""+a.toString()+"[dir=\"forward\"
			// arrowhead=onormal style=dashed]");
		}
	}
	try {
		String typesuperclass = type.getSuperclassType().toString();
		System.out.print(typeName + " [shape = \"record\", label = \"{");
	}
	catch (NullPointerException e) {
		System.out.print(typeName + " [shape = \"record\", label = \"{");
	}
	List<Object> aaaaa = type.modifiers();
	int cnt = 0;
	for (Object a : aaaaa) {
		if (a.toString().equals("abstract")) {
			cnt++;
		}
	}
	if (cnt >= 1) {
		System.out.print("\\<Abstract\\>");
	}
	if (type.isInterface() == true)
		System.out.print("\\<Interface\\>");
	System.out.print(typeName + "|");
	for (FieldDeclaration field : type.getFields()) {
		fieldDeclaration(field);
		System.out.print("\\l\n");
	}
	System.out.print("|");
	for (MethodDeclaration method : type.getMethods()) {
		methodDeclaration(method);
		System.out.print("\\l");
	}
	System.out.print(" }\"]\n");
	System.out.println();.
}

String bt;

public void fieldDeclaration(FieldDeclaration field) {
	List<Modifier> modifiers = field.modifiers();
	for (Modifier mo : modifiers) {
		modifiers(mo);
	}
	String field_name = field.fragments().get(0).toString().split("=")[0];
	char[] field_names = field_name.toCharArray();
	
	for (int i = 0; i < field_name.length(); i++) {
		if (field_name.charAt(i) == '<' || field_name.charAt(i) == '>') {
			System.out.print("\\");
		} else if (field_name.charAt(i) == '{' || field_name.charAt(i) == '}') {
			System.out.print("\\");
		} else if (field_name.charAt(i) == '"') {
			System.out.print("\\");
		}
		System.out.print(field_names[i]);
	}
	System.out.print(": ");
	// �ʵ� �ڷ���
	String field_type = field.getType().toString();
	char[] field_types = field_type.toCharArray();
	for (int i = 0; i < field_type.length(); i++) {
		if (field_type.charAt(i) == '<' || field_type.charAt(i) == '>') {
			System.out.print("\\");
		}
		System.out.print(field_types[i]);
	}
}

public void methodDeclaration(MethodDeclaration method) {
	method.getBody().statements().getClass().toString();
	System.out.print("\n");
	for (int i = 0; i < method.modifiers().size(); i++) {
		if (method.modifiers().get(0) instanceof Modifier) {
			modifiers((Modifier) method.modifiers().get(i));
		}

	}
	System.out.print(method.getName().toString() + "(");
	if (method.parameters().isEmpty() != true) {
		List<SingleVariableDeclaration> ps = method.parameters();
		int cnt = 0;
		for (SingleVariableDeclaration p : ps) {
			if (cnt > 0) {
				System.out.print(", ");
			}
			singleVariableDeclaration(p);
			cnt++;
		}
	}

	System.out.print("): " + method.getReturnType2());
}

public void modifiers(Modifier mo) {
	String keyword = mo.getKeyword().toString();
	if (keyword.equals("private"))
		System.out.print("-");
	else if (keyword.equals("public"))
		System.out.print("+");
	else if (keyword.equals("protected"))
		System.out.print("#");
}

public void singleVariableDeclaration(SingleVariableDeclaration s) {
	System.out.print(s.getName() + ":");
	String method_param1 = s.getType().toString();
	char[] method_params1 = method_param1.toCharArray();

	for (int i = 0; i < method_param1.length(); i++) {
		if (method_param1.charAt(i) == '<' || method_param1.charAt(i) == '>') {
			System.out.print("\\");
		} else if (method_param1.charAt(i) == '{' || method_param1.charAt(i) == '}') {
			System.out.print("\\");
		} else if (method_param1.charAt(i) == '"') {
			System.out.print("\\");
		}
		System.out.print(method_params1[i]);
	}

}

}*/