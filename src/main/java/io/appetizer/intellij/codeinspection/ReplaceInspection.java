package io.appetizer.intellij.codeinspection;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.ui.DocumentAdapter;
import io.appetizer.intellij.VariantPool;
import io.appetizer.intellij.remotecall.filenavigator.FileNavigator;
import io.appetizer.intellij.remotecall.filenavigator.FileNavigatorImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;




public class ReplaceInspection extends BaseJavaLocalInspectionTool {

  @SuppressWarnings({"WeakerAccess"}) @NonNls public String CHECKED_CLASSES = "java.lang.String;";
  @NonNls private static final String DESCRIPTION_TEMPLATE = InspectionsBundle.message("inspection.duplicates.replace.family.quickfix");
  private final LocalQuickFix myQuickFix = new MyQuickFix();
  private static final FileNavigator fileNavigator = new FileNavigatorImpl();

  @NotNull
  public String getDisplayName() {
    return " 'appetizer' instead of 'apetizer'";
  }

  @NotNull
  public String getGroupDisplayName() {
    return GroupNames.BUGS_GROUP_NAME;
  }

  @NotNull
  public String getShortName() {
    return "Replace";
  }

  private static class MyQuickFix implements LocalQuickFix {
    @NotNull
    @Override
    public String getName() {
      return "Go to see 'root cause'";
    }

    @NotNull
    @Override
    public String getFamilyName() {
      return getName();
    }

    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
      if (VariantPool.isJump()) {
        fileNavigator.findAndNavigate(VariantPool.getMyFileName(), VariantPool.getMyLine(), 2, 1);
      }
    }
  }

  private static boolean isFindYYY(String exp) {
    return exp.contains(VariantPool.getMyTaggedWords());
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
    return new JavaElementVisitor() {
      @Override
      public void visitReferenceExpression(PsiReferenceExpression psiReferenceExpression) {
      }

      @Override
      public void visitLocalVariable(PsiLocalVariable variable) {
        super.visitLocalVariable(variable);
        if (VariantPool.isJump()) {
          if (isFindYYY(variable.toString())) {
            holder.registerProblem(variable, DESCRIPTION_TEMPLATE, myQuickFix);
          }
        }
      }
    };
  }

  public JComponent createOptionsPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    final JTextField checkedClasses = new JTextField(CHECKED_CLASSES);
    checkedClasses.getDocument().addDocumentListener(new DocumentAdapter() {
      public void textChanged(DocumentEvent event) {
        CHECKED_CLASSES = checkedClasses.getText();
      }
    });
    panel.add(checkedClasses);
    return panel;
  }

  public boolean isEnabledByDefault() {
    return true;
  }
}


