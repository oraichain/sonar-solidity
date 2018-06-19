package org.sonarsource.solidity;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonarsource.solidity.frontend.SolidityParser;
import org.sonarsource.solidity.frontend.Utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MetricsVisitorTest {

  @Test
  public void test() {
    String filename = "test_contracts.sol";
    FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
    FileLinesContext fileLinesContext = mock(FileLinesContext.class);
    when(fileLinesContextFactory.createFor(any(InputFile.class))).thenReturn(fileLinesContext);
    InputFile file = createInputFile(new File("src/test/resources/" + filename));
    try {
      SolidityParser parser = Utils.returnParserUnitFromParsedFile(file.contents());
      MetricsVisitor metricsVisitor = new MetricsVisitor(parser);
      assertThat(metricsVisitor.fileMeasures.getContractNumber()).isEqualTo(2);
      assertThat(metricsVisitor.fileMeasures.getCommentLinesNumber()).isEqualTo(2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void test2() {
    String filename = "test1.sol";
    FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
    FileLinesContext fileLinesContext = mock(FileLinesContext.class);
    when(fileLinesContextFactory.createFor(any(InputFile.class))).thenReturn(fileLinesContext);
    InputFile file = createInputFile(new File("src/test/resources/" + filename));
    try {
      SolidityParser parser = Utils.returnParserUnitFromParsedFile(file.contents());
      MetricsVisitor metricsVisitor = new MetricsVisitor(parser);
      assertThat(metricsVisitor.fileMeasures.getContractNumber()).isEqualTo(1);
      assertThat(metricsVisitor.fileMeasures.getFunctionNumber()).isEqualTo(3);
      assertThat(metricsVisitor.fileMeasures.getStatementNumber()).isEqualTo(8);
      assertThat(metricsVisitor.fileMeasures.getCommentLinesNumber()).isEqualTo(5);
      assertThat(metricsVisitor.fileMeasures.getLinesOfCodeNumber()).isEqualTo(34);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void test3() {
    String filename = "test3.sol";
    FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
    FileLinesContext fileLinesContext = mock(FileLinesContext.class);
    when(fileLinesContextFactory.createFor(any(InputFile.class))).thenReturn(fileLinesContext);
    InputFile file = createInputFile(new File("src/test/resources/" + filename));
    try {
      SolidityParser parser = Utils.returnParserUnitFromParsedFile(file.contents());
      MetricsVisitor metricsVisitor = new MetricsVisitor(parser);
      assertThat(metricsVisitor.fileMeasures.getContractNumber()).isEqualTo(1);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private InputFile createInputFile(File file) {
    try {
      return TestInputFileBuilder.create("module", file.getName())
        .setModuleBaseDir(file.getParentFile().toPath())
        .setCharset(StandardCharsets.UTF_8)
        .setLanguage(Solidity.KEY)
        .initMetadata(new String(java.nio.file.Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8)).build();
    } catch (java.io.IOException e) {
      throw new IllegalStateException("File Not Found!", e);
    }
  }
}