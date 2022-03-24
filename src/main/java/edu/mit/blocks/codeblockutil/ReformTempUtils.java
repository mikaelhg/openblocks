package edu.mit.blocks.codeblockutil;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class ReformTempUtils
{

    /**
     * creates the shape of the track on the left side of the thumb
     * @param blueprint
     * @return general path shape of track on the left side of the thumb
     */
    public Shape reformLeadingTrack(SliderBlueprint blueprint) {
        GeneralPath shape = new GeneralPath();
        shape.moveTo(blueprint.closeTrackEdgeLeft, blueprint.trackTop);
        shape.lineTo(blueprint.thumbCenter, blueprint.trackTop);
        shape.lineTo(blueprint.thumbCenter, blueprint.trackBottom);
        shape.lineTo(blueprint.closeTrackEdgeLeft, blueprint.trackBottom);
        shape.curveTo(blueprint.farTrackEdgeLeft, blueprint.trackBottom,
                blueprint.farTrackEdgeLeft, blueprint.trackTop,
                blueprint.closeTrackEdgeLeft, blueprint.trackTop);
        shape.closePath();
        return shape;
    }

    /**
     * creates the shape of the track on the right side of the thumb
     * @param blueprint
     * @return general path shape of the track on the right side of the thumb
     */
    public Shape reformTrailingTrack(SliderBlueprint blueprint) {
        GeneralPath shape = new GeneralPath();
        shape.moveTo(blueprint.thumbCenter, blueprint.trackTop);
        shape.lineTo(blueprint.closeTrackEdgeRight, blueprint.trackTop);
        shape.curveTo(blueprint.farTrackEdgeRight, blueprint.trackTop,
                blueprint.farTrackEdgeRight, blueprint.trackBottom,
                blueprint.closeTrackEdgeRight, blueprint.trackBottom);
        shape.lineTo(blueprint.thumbCenter, blueprint.trackBottom);
        shape.lineTo(blueprint.thumbCenter, blueprint.trackTop);
        shape.closePath();
        return shape;
    }

    /**
     * creates the shape of the thumb
     * @param blueprint
     * @param diameter
     * @return ellipse of the thumb
     */
    public Shape reformThumb(SliderBlueprint blueprint, int diameter) {
        return new Ellipse2D.Double(blueprint.thumbCenter - diameter / 2,
                blueprint.trackMiddleY - diameter / 2, diameter, diameter);
    }

    /**
     * creates the shape of the ticks
     * @param blueprint
     * @return general path of the ticks
     */
    public Shape reformTicks(SliderBlueprint blueprint, Integer tickNumber) {
        GeneralPath ticks = new GeneralPath();
        int count = 0;
        float position = blueprint.closeTrackEdgeLeft;
        float interval = (float) (blueprint.closeTrackEdgeRight - blueprint.closeTrackEdgeLeft) / tickNumber;
        while (count < (tickNumber + 1)) {
            ticks.moveTo((int) position, blueprint.trackTop);
            ticks.lineTo((int) position, blueprint.trackBottom);
            position += interval;
            count += 1;
        }
        ticks.closePath();
        return ticks;
    }

    /**
     * Sets the values in the class SliderBlueprint
     * @param blueprint
     * @param width
     * @param height
     * @param girth
     * @param thumbX - distance from the upper left corner to the center of the thumb
     */
    public void reformBlueprint(SliderBlueprint blueprint,
                                int width, int height, int girth, int thumbX) {
        blueprint.farTrackEdgeLeft = height / 2 - girth / 2;
        blueprint.closeTrackEdgeLeft = height / 2;
        blueprint.thumbCenter = thumbX;
        blueprint.closeTrackEdgeRight = width - height / 2;
        blueprint.farTrackEdgeRight = width - height / 2 + girth / 2;
        blueprint.trackTop = height / 2 - girth / 2;
        blueprint.trackMiddleY = height / 2;
        blueprint.trackBottom = height / 2 + girth / 2;
    }
}
