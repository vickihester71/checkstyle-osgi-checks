package com.adobe.acs.checkstyle.osgi.javadoc;

import java.util.Arrays;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

class Utilities {
    
    private Utilities() {}

    static boolean isExportedPackage(DetailAST aAST) {
        DetailAST packageAST = findPackage(aAST);

        if (packageAST == null) {
            System.err.println("No package available for " + aAST);
            return true;
        }

        String packageName = getPackageName(packageAST);
        String[] parts = packageName.split("\\.");
        Arrays.sort(parts);

        boolean exported = Arrays.binarySearch(parts, "impl") < 0 && Arrays.binarySearch(parts, "internal") < 0;

        return exported;
    }

    private static String getPackageName(DetailAST packageAST) {
        final DetailAST nameAST = packageAST.getLastChild().getPreviousSibling();
        final FullIdent full = FullIdent.createFullIdent(nameAST);
        return full.getText();
    }

    private static DetailAST findPackage(DetailAST aAST) {
        if (aAST == null) {
            return null;
        }

        if (aAST.getType() == TokenTypes.PACKAGE_DEF) {
            return aAST;
        }
        if (aAST.getType() == TokenTypes.CLASS_DEF) {
            return findPackage(aAST.getPreviousSibling());
        }

        return findPackage(aAST.getParent());
    }

}
