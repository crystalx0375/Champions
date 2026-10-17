package crystal.champions;

public interface IChampions {
    int champions$getChampionTier();
    void champions$setChampionTier(int value);
    String champions$getAffixesString();
    void champions$setAffixesString(String value);
    String champions$getAdaptationType();
    void champions$setAdaptationType(String value);
    int champions$getAdaptation();
    void champions$setAdaptation(int value);
    boolean champions$isShielding();
    default void champions$setShielding(boolean value) {}
}
