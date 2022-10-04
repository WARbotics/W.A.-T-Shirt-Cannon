package frc.robot.common;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Test;

import edu.wpi.first.wpilibj.Filesystem;
import frc.robot.common.TrajectoryImporter;

public class AutoComandTest {

    @Test
    public void TrajectoryImportTest() {

        try {
            Path path = TrajectoryImporter.getTrajectory("deploy/paths/test.wpilib.json");
            assertEquals(Filesystem.getDeployDirectory().toPath().resolve("deploy/paths/test.wpilib.json"), path);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
