package spinkit;



import spinkit.sprite.Sprite;
import spinkit.style.ChasingDots;
/*import spinkit.style.Circle;
import spinkit.style.CubeGrid;
import spinkit.style.DoubleBounce;
import spinkit.style.FadingCircle;
import spinkit.style.FoldingCube;
import spinkit.style.MultiplePulse;
import spinkit.style.MultiplePulseRing;
import spinkit.style.Pulse;
import spinkit.style.PulseRing;
import spinkit.style.RotatingCircle;
import spinkit.style.RotatingPlane;
import spinkit.style.ThreeBounce;
import spinkit.style.WanderingCubes;
import spinkit.style.Wave;*/

/**
 * Created by ybq.
 */
public class SpriteFactory {

    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {
            case CHASING_DOTS:
                sprite = new ChasingDots();
                break;
          /*  case ROTATING_PLANE:
                sprite = new RotatingPlane();
                break;
            case DOUBLE_BOUNCE:
                sprite = new DoubleBounce();
                break;
            case WAVE:
                sprite = new Wave();
                break;
            case WANDERING_CUBES:
                sprite = new WanderingCubes();
                break;
            case PULSE:
                sprite = new Pulse();
                break;
            case THREE_BOUNCE:
                sprite = new ThreeBounce();
                break;
            case CIRCLE:
                sprite = new Circle();
                break;
            case CUBE_GRID:
                sprite = new CubeGrid();
                break;
            case FADING_CIRCLE:
                sprite = new FadingCircle();
                break;
            case FOLDING_CUBE:
                sprite = new FoldingCube();
                break;
            case ROTATING_CIRCLE:
                sprite = new RotatingCircle();
                break;
            case MULTIPLE_PULSE:
                sprite = new MultiplePulse();
                break;
            case PULSE_RING:
                sprite = new PulseRing();
                break;
            case MULTIPLE_PULSE_RING:
                sprite = new MultiplePulseRing();
                break;*/
            default:
                break;
        }
        return sprite;
    }
}
