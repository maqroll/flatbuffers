package flatbuffers;

import static org.junit.Assert.assertEquals;

import com.google.flatbuffers.FlatBufferBuilder;
import org.junit.Test;
import test.flatbuffers.Color;
import test.flatbuffers.Equipment;
import test.flatbuffers.Monster;
import test.flatbuffers.Vec3;
import test.flatbuffers.Weapon;

public class AppTest {

  @Test
  public void encodeAndDecodeTest() {
    FlatBufferBuilder builder = new FlatBufferBuilder(1024);

    int weaponOneName = builder.createString("Sword");
    short weaponOneDamage = 3;

    int weaponTwoName = builder.createString("Axe");
    short weaponTwoDamage = 5;

    int sword = Weapon.createWeapon(builder, weaponOneName, weaponOneDamage);
    int axe = Weapon.createWeapon(builder, weaponTwoName, weaponTwoDamage);

    int name = builder.createString("Orc");

    byte[] treasure = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    int inv = Monster.createInventoryVector(builder, treasure);

    int[] weaps = new int[2];
    weaps[0] = sword;
    weaps[1] = axe;

    int weapons = Monster.createWeaponsVector(builder, weaps);

    Monster.startPathVector(builder, 2);
    Vec3.createVec3(builder, 1.0f, 2.0f, 3.0f);
    Vec3.createVec3(builder, 4.0f, 5.0f, 6.0f);
    int path = builder.endVector();

    Monster.startMonster(builder);
    Monster.addPos(builder, Vec3.createVec3(builder, 1.0f, 2.0f, 3.0f));
    Monster.addName(builder, name);
    Monster.addColor(builder, Color.Red);
    Monster.addHp(builder, (short)300);
    Monster.addInventory(builder, inv);
    Monster.addWeapons(builder, weapons);
    Monster.addEquippedType(builder, Equipment.Weapon);
    Monster.addEquipped(builder, axe);
    Monster.addPath(builder, path);
    int orc = Monster.endMonster(builder);

    builder.finish(orc);

    java.nio.ByteBuffer buf = builder.dataBuffer();

    //------------------------------------------------------------
    System.out.println(buf.remaining());
    //------------------------------------------------------------

    Monster monster = Monster.getRootAsMonster(buf);

    assertEquals((short)300,monster.hp());
    assertEquals("Orc",monster.name());
  }
}
