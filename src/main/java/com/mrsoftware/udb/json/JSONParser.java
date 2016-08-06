package com.mrsoftware.udb.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Stack;

abstract public class JSONParser {

    boolean inQuotes = false;

    boolean haveElementName = false;

    boolean haveValue = false;

    String lastName = null;
    JSONValue lastValue = null;
    String currentName = null;

    int quoteCount = 0;
    int entityCount = 0;
    boolean haveName = false;
    int jsonLength = 0;
    boolean inArray = false;
    boolean isString = false;

    StringBuilder sb = null;

    Object result = null;

    abstract public Object onBeginObject(String name);		// Returned object is pushed onto stack

    abstract public Object onBeginArray(String name);		// Returned object is pushed onto stack

    abstract public void addNamedValue(String name, JSONValue value);

    abstract public void addChildObject(String name, Object object);

    abstract public void addArrayElement(Object object);

    abstract public void onName(String name);

    abstract public boolean isObject(Object o);		// Derived class returns true if the passed type is an object

    abstract public boolean isArray(Object o);		// Derived class returns true if the passed type is an Array

    Stack<String> elementNames = new Stack<String>();	// Element names at nesting level (first entity does name have a name, nested ones do)

    String lastClosure = "";

    int bufferCount = 0;

    protected Stack<Object> stack = new Stack<Object>();

    public boolean onObjectEnd() {
        return true;
    }

    public boolean onArrayEnd() {
        return true;
    }

    public JSONParser() {
    }

    private String loadFile(String name) throws JSONParserException {
        StringBuffer stringBuffer = new StringBuffer();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(name));

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            bufferedReader.close();
        } catch (Exception e) {
            throw new JSONParserException(e, "Failed to load JSON file contents!");
        }

        return stringBuffer.toString();
    }

    public void parseFile(String fileName) throws JSONParserException {
        String content = loadFile(fileName);

        parse(content);
    }

    StringBuilder parsedText = new StringBuilder();

    public void parse(String jsonString) throws JSONParserException {
        boolean done = false;

        resetStringBuffer();

        char c = 0;

        try {

            for (int charPos = 0; charPos < jsonString.length(); charPos++) {
                try {
                    c = jsonString.charAt(charPos);
                } catch (Exception e) {
                    done = true;
                }

                // In the following code...
                // The first check is to see if we are in quoted text
                // If so, then all characters up to the closing " go into the buffer
                // This happens for all names and some values
                // If we are not in quotes, then the parser checks for structural elements ({},[])
                // And processes accordingly.  Most of the time this means a call to the derived parser
                // If it is not a structural character, then it goes into the buffer
                // This would most likely be a numeric value
                if ('\"' == c) {
                    inQuotes = !inQuotes;

                    isString = true;
                } else if (Character.isWhitespace(c) & !inQuotes) {
                    c = 0;
                } else if ("{[}],:".contains(String.valueOf(c)) & !inQuotes) {
                    if ('{' == c) {
                        handleObjectStart();
                    } else if ('[' == c) {
                        handleArrayStart();

                    } else if ('}' == c) {
                        handleObjectEnd();

                    } else if (',' == c) {
                        handleNextElement();
                    } else if (']' == c) {
                        handleArrayEnd();

                    } else if (':' == c) {
                        handleEndOfName();
                    }
                } else {
                    addToBuffer(c);
                }

                parsedText.append(c);
            }
        } catch (Exception e) {
            throw new JSONParserException(e, "JSON Parsing exception occurred at: ", parsedText.toString());
        }
    }

    private void addToBuffer(char c) {
        sb.append(c);
        bufferCount++;
    }

    private boolean isBuffer() {
        return bufferCount > 0;
    }

    private String getBufferName() {
        String retval = sb.toString();

        resetStringBuffer();

        return retval;
    }

    private JSONValue getBufferValue() {
        String retval = sb.toString();

        JSONValue value = new JSONValue(retval, isString);

        resetStringBuffer();

        return value;
    }

    protected void resetStringBuffer() {
        sb = new StringBuilder();
        bufferCount = 0;
        isString = false;
    }

    private void handleEndOfName() {
        lastName = currentName;

        currentName = getBufferName();

        lastClosure = "N";

        onName(currentName);
    }

    private void handleObjectStart() {
        String name = getElementName();

        Object o = onBeginObject(name);

        if (o != null) {
            pushContext(o);
        }
    }

    private void handleArrayStart() {
        inArray = true;

        Object o = onBeginArray(getElementName());

        if (o != null) {
            pushContext(o);
        }
    }

    private void handleObjectEnd() {
        if (isBuffer()) // handle last value 
        {
            handleNamedValue();
        }

        lastClosure = "O";

        resetStringBuffer();
        currentName = null;

        popContext();
    }

    protected boolean inArray() {
        return this.inArray;
    }

    private void handleArrayEnd() {
        if (isBuffer()) // Handle the last value in a non-object array
        {
            addArrayElement(getBufferValue());
        }

        inArray = false;

        lastClosure = "A";

        if (onArrayEnd()) {
            popContext();
        }
    }

    public boolean verifyPopContext() {
        return true;
    }

    public void popContext() {
        if (verifyPopContext()) {
            if (!stack.isEmpty()) {
                stack.pop();
            }
        }
    }

    // This is called on encountering a comma
    // The activity to do is dependent on what was encountered before the comma
    private void handleNextElement() {
        if (isBuffer()) {
            handleNamedValue();
        }
    }

    public void handleNamedObject() {
        //		if ("OA".contains(lastClosure))
        //		{
        //			Object child = stack.pop();
        //		
        //			String name = getElementName();
        //			
        //			if (isArray(getCurrentContext()))
        //				addArrayElement(child);
        //			
        //			else
        //				addChildObject(name, child);
        //		}
        //		else
        //		{
        //			String name = getElementName();
        //			
        //			addNamedValue(name, sb.toString());
        //		}
        //		
        //		lastClosure = "";
        //		
        //		resetStringBuffer();
    }

    public void handleNamedValue() {
        String name = getElementName();

        JSONValue value = getBufferValue();

        addNamedValue(name, value);
    }

    private String getElementName() {
        return currentName;
    }

    //	private boolean hasParentContext()
    //	{
    //		return !stack.isEmpty() && stack.size() > 1;
    //	}
    //	private boolean isRoot()
    //	{
    //		return !stack.isEmpty() && stack.size() == 1;
    //	}
    public <T> T getCurrentContext() {
        if (stack.isEmpty()) {
            return null;
        } else {
            return (T) stack.peek();
        }
    }

    public <T> T getResult() {
        // Clean up for next time
        stack.clear();

        return (T) result;
    }

    public String getLastName() {
        return elementNames.pop();
    }

    protected void pushContext(Object o) {
        if (stack.isEmpty()) {
            result = o;
        }

        //		else
        //			// Allow the current context to add the new context in its own way
        //			addChildObject(getElementName(), o);
        stack.push(o);

        resetStringBuffer();
    }
}
