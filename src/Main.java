import java.util.Random;

public class Main {
    public static int bossHealth = 700;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {270, 280, 250,400,500,300,240,290};
    public static int[] heroesDamage = {20, 10, 15,0,5,0,0,0};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic","Medic","Golem","Whitcher","Lucky","Thor"};
    public static int medicHealAmount = 20;
    public static int roundNumber = 0;
    public static boolean thorStunned;
    public static boolean luckyDodgeChance;
    public static boolean witcherReviveChance;


    public static void main(String[] args) {
        showStatistics();
        while (!isGameOver()) {
            playRound();
        }
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }

        /*if (heroesHealth[0] <= 0 && heroesHealth[1] <= 0 && heroesHealth[2] <= 0) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;*/
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }

    public static void playRound() {
        roundNumber++;
        chooseDefence();
        bossAttacks();
        medicHeal();
        heroesAttack();
        showStatistics();
    }

    public static void chooseDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length); // 0,1,2
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void heroesAttack() {
        Random random = new Random();
        luckyDodgeChance = random.nextBoolean();
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                luckyDodgeChance = random.nextBoolean();
                if (i == 6 && luckyDodgeChance) {
                    System.out.println("Lucky dodged the boss's attack!");
                    continue;
                }
                if (heroesAttackType[i] == bossDefence) {
                    int coeff = random.nextInt(9) + 2; //2,3,4,5,6,7,8,9,10
                    damage = heroesDamage[i] * coeff;
                    System.out.println("Critical damage: " + damage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth = bossHealth - damage;
                }
            }
        }
    }

    public static void bossAttacks() {
        Random random = new Random();
        thorStunned = random.nextBoolean();
        witcherReviveChance = random.nextBoolean();
        if (!thorStunned) {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] > 0) {
                    if (heroesHealth[i] - bossDamage < 0) {
                        heroesHealth[i] = 0;
                    }
                    int damage = bossDamage;
                    if ("Golem" == heroesAttackType[i]) {
                        damage /= 5;
                        System.out.println("Golem absorbs damage!");
                    } else if ("Witcher" == heroesAttackType[i] && witcherReviveChance && isAnyHeroDead()) {
                        reviveRandomHero();
                        return; 
                    }  if (heroesHealth[i] - damage < 0) {
                        heroesHealth[i] = 0;
                    }
                    else {
                        heroesHealth[i] = heroesHealth[i] - bossDamage;
                    }
                }
            }
        } else if (thorStunned && heroesHealth[7] > 0) {
            System.out.println("Thor stunned the boss! The boss skips this round.");
        }
    }
    public static void medicHeal() {
        if (heroesHealth[3]> 0) {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] < 100 && heroesHealth[i] > 0 &&  i != heroesHealth.length - 1) {
                    heroesHealth[i] += medicHealAmount;
                    if (heroesHealth[i] > 100) {
                        heroesHealth[i] = 100;
                    }
                    System.out.println("Medic healed hero " + (i + 1) + " for " + medicHealAmount + " health points");
                    break;
                }
            }
        } else {
            System.out.println("Medic is dead and can't heal");
        }
    }
    public static void reviveRandomHero() {
        Random random = new Random();
        int i = random.nextInt(heroesHealth.length);
        while (heroesHealth[i] > 0) {
            i = random.nextInt(heroesHealth.length);
        }
        heroesHealth[i] = 100;
        System.out.println("Witcher sacrificed himself to revive a fallen hero!");
    }

    public static boolean isAnyHeroDead() {
        for (int health : heroesHealth) {
            if (health <= 0) {
                return true;
            }
        }
        return false;
    }


    public static void showStatistics() {
        System.out.println("ROUND " + roundNumber + " ----------------");
        /*String defence;
        if (bossDefence == null) {
            defence = "No defence";
        } else {
            defence = bossDefence;
        }*/
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defence: " +
                (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " damage: " + heroesDamage[i]);
        }
    }
}