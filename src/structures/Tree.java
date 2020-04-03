package structures;

import java.util.*;
import java.util.jar.Attributes.Name;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode,
 * with fields for tag/text, first child and sibling.
 * 
 */
public class Tree {
	/**
	 * Root node
	 */
	TagNode root = null;

	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;

	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed in to the
	 * constructor and stored in the sc field of this object.
	 * 
	 * The root of the tree that is built is referenced by the root field of this
	 * object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		
		Stack<TagNode> tags = new Stack<TagNode>();
		TagNode temp = null;
		TagNode endSibling = null;
		String currentLine = "";
		root = new TagNode(sc.nextLine().replaceAll("<|>", ""),null,null);
		tags.push(root);
		
		while(sc.hasNextLine()){
			currentLine = sc.nextLine();
			if(currentLine.equals("</body>"))
				return;
			if(currentLine.contains("<") && currentLine.contains(">") && currentLine.contains("/")) {
				tags.pop();
			}
			else if(currentLine.contains("<") && currentLine.contains(">") && !currentLine.contains("/")) {
				if(tags.peek().firstChild==null) {
					temp = new TagNode(currentLine.replaceAll("<|>", ""),null,null);
					tags.peek().firstChild = temp;
					tags.push(temp);
				}else {
					endSibling = tags.peek().firstChild;
					while(endSibling.sibling!=null) {
						endSibling = endSibling.sibling;
					}
					temp = new TagNode(currentLine.replaceAll("<|>", ""),null,null);
					endSibling.sibling = temp;
					tags.push(temp);
				}				
			}else {
				if(tags.peek().firstChild==null) {
					tags.peek().firstChild = new TagNode(currentLine,null,null);
				}else {
					endSibling = tags.peek().firstChild;
					while(endSibling.sibling!=null) {
						endSibling = endSibling.sibling;
					}
					endSibling.sibling = new TagNode(currentLine,null,null);;
				}				
			}
		}
		
		
		/*
		root = new TagNode(sc.nextLine().replaceAll("<|>", ""),new TagNode(sc.nextLine().replaceAll("<|>", ""),null,null),null);
		TagNode ptr = root;
		ptr = ptr.firstChild;
		TagNode temp = null;
		TagNode tempPtr = temp;
		Stack<String> tags = new Stack<String>();
		String lineText = "";
		
		while(sc.hasNextLine()) {
			lineText = sc.nextLine();
			if(lineText.equals("</".concat(tags.peek().concat(">")))) {
				temp = new TagNode(tags.pop(),temp,null);
				if(root.firstChild.firstChild==null) {
					ptr.firstChild = temp;
					ptr = ptr.firstChild;
				}
				else {
					ptr.sibling = temp;
					ptr = ptr.sibling;
				}
				temp = null;
			}else if(lineText.contains("<") && !lineText.contains("/")) {
				tags.push(lineText.replaceAll("<|>", ""));
			}else {
				if(temp == null) {
					temp = new TagNode(lineText.replaceAll("<|>", ""),null,null);
					tempPtr = temp;
				}else {
					tempPtr.firstChild = new TagNode(lineText.replaceAll("<|>", ""),null,null);
					tempPtr = tempPtr.firstChild;
				}
			}
		}
		*/
		
//		Stack<String> stk = new Stack<String>();
//		TagNode ptr = null;
//		TagNode ptr1 = root;
//		String name = "";
//		boolean tf = false;	
//		while (sc.hasNextLine()) {
//			name = sc.nextLine();
//			if (name.contains("/") || name.equals("<em>")) {
//				ptr = null;
//				while (!stk.isEmpty()) {
//					ptr = new TagNode(stk.pop(), ptr, null);
//				}
//				if(name.equals("<em>")) stk.push(name);
//				if(root == null) {
//					root = ptr;
//				}else if(ptr!=null){
//					tf = false;
//					System.out.println(ptr);
//					ptr1 = root;
//					while(ptr1!=null) {
//						while(ptr1.sibling!=null) {
//							if(ptr1.sibling == null) {
//								break;
//							}
//							ptr1 = ptr1.sibling;
//						}
//						if(ptr.tag.equals(ptr1.tag)) {
//							tf = true;
//							break;
//						}
//						if(ptr1.firstChild == null)
//							break;
//						ptr1 = ptr1.firstChild;
//					}
//					if(tf) {
//						ptr1.sibling = ptr;
//					}else if(ptr.tag.equals("<em>") && ptr1.tag.contains("<")) {
//						ptr1.firstChild = ptr;
//					}else if(ptr.tag.equals("<em>")){
//						ptr1.sibling = ptr;
//					}else{
//						ptr1 = root.firstChild.firstChild;
//						while(ptr1.sibling!=null) {
//							if(ptr1.sibling == null) {
//								break;
//							}
//							ptr1 = ptr1.sibling;
//						}
//						ptr1.sibling = ptr;
//					}
//				}
//			}
//			else {
//				stk.push(name);
//			}
//		}
	}

	private void replaceTagHelp(TagNode ptr, String oldTag, String newTag) {
		if(ptr == null) {
			return;
		}else if(ptr.tag.equals(oldTag)) {
			ptr.tag = newTag;
		}else {
			replaceTagHelp(ptr.firstChild,oldTag,newTag);
			replaceTagHelp(ptr.sibling,oldTag,newTag);
		}
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		TagNode ptr = root;
		replaceTagHelp(ptr,oldTag,newTag);
	}

	private void boldRowHelpHelper(TagNode ptr){
		if(ptr == null) {
			return;
		}else if(ptr.sibling==null) {
			ptr.firstChild = new TagNode("b",ptr.firstChild,null);
		}else {
			System.out.println(ptr.tag);
			ptr.firstChild = new TagNode("b",ptr.firstChild,null);
		}
	}
	
	private void boldRowHelp(int row, int counter, TagNode ptr) {
		if(ptr == null) {
			return;
		}else if(ptr.tag.equals("tr")) {
			counter++;
			if(counter==row) {
				boldRowHelpHelper(ptr.firstChild);
			}
		}
		boldRowHelp(row,counter,ptr.firstChild);
		boldRowHelp(row,counter,ptr.sibling);
		
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The
	 * boldface (b) tag appears directly under the td tag of every column of this
	 * row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		TagNode ptr = root;
		int counter = 0;
		boldRowHelp(row,counter,ptr);
	}
	
	private void removeTagHelpHelper(TagNode ptr) {
		if(ptr==null)
			return;
		else if(ptr.tag.equals("li"))
			ptr.tag = "p";
		removeTagHelpHelper(ptr.sibling);
	}
	
	private void removeTagHelp(String tag, TagNode ptr) {
		if(ptr == null) {
			return;
		}else if(ptr.firstChild != null && ptr.firstChild.tag.equals(tag)) {
			if(ptr.firstChild.sibling!=null) {
				TagNode helper = ptr.firstChild.firstChild;
				while(helper.sibling!=null) {
					helper = helper.sibling;
				}
				helper.sibling = ptr.firstChild.sibling;
			}
			ptr.firstChild = ptr.firstChild.firstChild;
			removeTagHelpHelper(ptr.firstChild);
		}else if(ptr.sibling != null && ptr.sibling.tag.equals(tag)) {
			if(ptr.sibling.sibling!=null) {
				TagNode helper = ptr.sibling;
				while(helper.firstChild!=null) {
					helper = helper.firstChild;
				}
				helper.sibling = ptr.sibling.sibling;
			}
			ptr.sibling = ptr.sibling.firstChild;
			removeTagHelpHelper(ptr);
		}
		removeTagHelp(tag,ptr.firstChild);
		removeTagHelp(tag,ptr.sibling);
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b,
	 * all occurrences of the tag are removed. If the tag is ol or ul, then All
	 * occurrences of such a tag are removed from the tree, and, in addition, all
	 * the li tags immediately under the removed tag are converted to p tags.
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		TagNode ptr = root;
		removeTagHelp(tag,ptr);
	}
	
	private void addTagHelp(String word, String tag, TagNode ptr) {
		if(ptr==null) {
			return;
		}else if(ptr.tag.toLowerCase().contains(word.toLowerCase())){
			if(ptr.tag.equalsIgnoreCase(word)) {
				ptr.firstChild = new TagNode(ptr.tag,ptr.firstChild,null);
				ptr.tag = tag;
			}else if(ptr.tag.toLowerCase().contains(word.toLowerCase())) {
				String helper = "";
				String strWord = "";
				String[] tagLine = ptr.tag.split(" ");
				for(int i=0; i<tagLine.length; i++) {
					if(tagLine[i].toLowerCase().equals(word.toLowerCase()) || (tagLine[i].length()==word.length()+1 && 
							(tagLine[i].charAt(word.length())==';' ||
							tagLine[i].charAt(word.length())==',' ||
							tagLine[i].charAt(word.length())==':' ||
							tagLine[i].charAt(word.length())=='.' ||
							tagLine[i].charAt(word.length())=='?' ||
							tagLine[i].charAt(word.length())=='!' ))) {
						strWord = tagLine[i];
						String temp = "";
						if(!helper.equals("")) {
							for(int j=i+1; j<tagLine.length; j++) {
								if(j == tagLine.length-1)
									temp = temp.concat(tagLine[j]);
								else
									temp = temp.concat(tagLine[j].concat(" "));
							}
							TagNode t = null;
							if(temp.equals("")) {
								t = new TagNode(strWord,null,null);
							}else {
								t = new TagNode(strWord.concat(" " + temp),null,null);
							}
							ptr.tag = helper;
							helper = "";
							ptr.sibling = t;
							addTagHelp(word,tag,ptr.sibling);
							break;
						}else{
							for(int j=i+1; j<tagLine.length; j++) {
								if(j == tagLine.length-1)
									temp = temp.concat(tagLine[j]);
								else
									temp = temp.concat(tagLine[j].concat(" "));
							}
							TagNode t = new TagNode(temp,null,null);
							ptr.tag = tag;
							ptr.firstChild = new TagNode(strWord, null, null);
							helper = "";
							if(!temp.equals("")){
								ptr.sibling = t;
								addTagHelp(word,tag,ptr.sibling);
							}else if(temp.equals("")) {
								ptr.sibling = null;
							}
							break;
						}
					}else {
						helper = helper.concat(tagLine[i].concat(" "));
					}
				}
			}
			
		}else {
			addTagHelp(word,tag,ptr.firstChild);
			addTagHelp(word,tag,ptr.sibling);
		}
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag  Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		TagNode ptr = root;
		addTagHelp(word,tag,ptr);
	}

	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes new
	 * lines, so that when it is printed, it will be identical to the input file
	 * from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines.
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}

	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr = root; ptr != null; ptr = ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");
			}
		}
	}

	/**
	 * Prints the DOM tree.
	 *
	 */
	public void print() {
		print(root, 1);
	}

	private void print(TagNode root, int level) {
		for (TagNode ptr = root; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < level - 1; i++) {
				System.out.print("      ");
			}
			;
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level + 1);
			}
		}
	}
}
