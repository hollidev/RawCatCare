package edu.lapinamk.holli.rawcatcare;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Cat
{
    public static final int CAT_GENDER_MALE = 0;
    public static final int CAT_GENDER_FEMALE = 1;
    public static final int CAT_STATE_NURSING = 1;
    public static final int CAT_STATE_NEUTERED = 1;
    public static final int CAT_STATE_NEGATIVE = 0;
    public static final int CAT_SHAPE_LEAN = 0;
    public static final int CAT_SHAPE_NORMAL = 1;
    public static final int CAT_SHAPE_OVERWEIGHT = 2;
    public static final String[] CAT_SHAPES = {"lean", "normal", "overweight"};
    public static final String[] CAT_ACTIVITY_LEVELS = {"low", "medium", "high"};

    private int id;
    private String name;
    private float weight;
    private Date dateOfBirth;
    private int shape;
    private int activityLevel;
    private int gender;
    private int neuteredState;
    private int nursingState;
    private String breed;
    private String avatarPath;

    public Cat(String name, float weight, Date dateOfBirth, int shape, int activityLevel, int gender, int neuteredState, int nursingState, String breed, String avatarPath)
    {
        this.name = name;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
        this.shape = shape;
        this.activityLevel = activityLevel;
        this.gender = gender;
        this.neuteredState = neuteredState;
        this.nursingState = nursingState;
        this.breed = breed;
        this.avatarPath = avatarPath;
    }

    public Cat(String name, String breed, Date dateOfBirth, float weight, int gender, String avatarPath, int activityLevel, int neuteredState, int shape, int nursingState)
    {
        this.name = name;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
        this.shape = shape;
        this.activityLevel = activityLevel;
        this.gender = gender;
        this.neuteredState = neuteredState;
        this.nursingState = nursingState;
        this.breed = breed;
        this.avatarPath = avatarPath;
    }

    public Cat(int id, String name, String avatarPath)
    {
        this.id = id;
        this.name = name;
        this.avatarPath = avatarPath;
    }

    private double calculateOptimalCalories()
    {
        double optimalCalories = 0;

        // if cat is growing
        if (getAgeInMonths() < 7)
        {
            optimalCalories = (48.502 * weight + 93.333);
        }
        // if cat is nursing
        else if (nursingState == CAT_STATE_NURSING)
            optimalCalories = (110.8 * weight + 92);
        else
        {
            // check the shape of the cat
            switch (shape)
            {
                // lean cat
                case CAT_SHAPE_LEAN:
                    optimalCalories = (38.77 * weight + 95.714);
                    break;
                // normal cat
                case CAT_SHAPE_NORMAL:
                    optimalCalories = (25.117 * weight + 134.64);
                    break;
                // overweight cat
                case CAT_SHAPE_OVERWEIGHT:
                    optimalCalories = (19.432 * weight + 139.29);
            }
        }

        return optimalCalories;
    }

    public HashMap<String, Double> getWeeklyNeeds()
    {
        HashMap<String, Double> weeklyNeeds = getDailyNeeds();

        // put the daily needs in the map, multiplying them with the number of days
        for (String key : weeklyNeeds.keySet())
            weeklyNeeds.put(key, weeklyNeeds.get(key) * 7);

        weeklyNeeds.put("fatProteinRatio", getOptimalFatProteinRatio());
        weeklyNeeds.put("caToPRatio", CatNeeds.OPTIMAL_PHOSPHORUS_TO_CALCIUM_RATIO);

        return weeklyNeeds;
    }

    private HashMap<String, Double> getDailyNeeds()
    {
        HashMap<String, Double> needs = new HashMap<>();

        double optimalCalories = calculateOptimalCalories();
        double calcium = Cat.CatNeeds.DAILY_CALCIUM * weight;
        double phosphorus = CatNeeds.DAILY_PHOSPHORUS * weight;
        double magnesium = CatNeeds.DAILY_MAGNESIUM * weight;
        double selenium = CatNeeds.DAILY_SELENIUM * weight;
        double iodine = CatNeeds.DAILY_IODINE * weight;
        double iron = CatNeeds.DAILY_IRON * weight;
        double copper = CatNeeds.DAILY_COPPER * weight;
        double sodium = CatNeeds.DAILY_SODIUM * weight;
        double potassium = CatNeeds.DAILY_POTASSIUM * weight;
        double chlorine = CatNeeds.DAILY_CHLORINE * weight;
        double zinc = CatNeeds.DAILY_ZINC * weight;
        double manganese = CatNeeds.DAILY_MANGANESE * weight;

        double vitA = CatNeeds.DAILY_VITAMIN_A * weight;
        double vitB1 = CatNeeds.DAILY_VITAMIN_B1 * weight;
        double vitB2 = CatNeeds.DAILY_VITAMIN_B2 * weight;
        double vitB3 = CatNeeds.DAILY_VITAMIN_B3 * weight;
        double vitB5 = CatNeeds.DAILY_VITAMIN_B5 * weight;
        double vitB6 = CatNeeds.DAILY_VITAMIN_B6 * weight;
        double vitB9 = CatNeeds.DAILY_VITAMIN_B9 * weight;
        double vitB12 = CatNeeds.DAILY_VITAMIN_B12 * weight;
        double vitD = CatNeeds.DAILY_VITAMIN_D * weight;
        double vitK = CatNeeds.DAILY_VITAMIN_K * weight;
        double vitE = CatNeeds.DAILY_VITAMIN_E * weight;

        // put the values in the map
        // can also be done in a loop
        needs.put("calcium", UnitConverter.convertMicrogramsToGrams(calcium));
        needs.put("calories", optimalCalories);
        needs.put("magnesium", magnesium);
        needs.put("phosphorus", UnitConverter.convertMicrogramsToGrams(phosphorus));
        needs.put("copper", copper);
        needs.put("A", vitA);
        needs.put("B12", vitB12);
        needs.put("E", vitE);
        needs.put("selenium", selenium);
        needs.put("B1", vitB1);
        needs.put("iron", iron);
        needs.put("sodium", sodium);
        needs.put("potassium", potassium);
        needs.put("chlorine", chlorine);
        needs.put("zinc", zinc);
        needs.put("manganese", manganese);
        needs.put("iodine", iodine);
        needs.put("K", vitK);
        needs.put("D", vitD);
        needs.put("B3", vitB3);
        needs.put("B2", vitB2);
        needs.put("B5", vitB5);
        needs.put("B6", vitB6);
        needs.put("B9", vitB9);

        return needs;
    }

    public boolean checkValues(Cat cat)
    {



        return true;
    }

    public float getWeight()
    {
        return weight;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }

    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public int getShape()
    {
        return shape;
    }

    public void setShape(int shape)
    {
        this.shape = shape;
    }

    public int getActivityLevel()
    {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel)
    {
        this.activityLevel = activityLevel;
    }

    public int getNeuteredState()
    {
        return neuteredState;
    }

    public void setNeuteredState(int neuteredState)
    {
        this.neuteredState = neuteredState;
    }

    public int getNursingState()
    {
        return nursingState;
    }

    public void setNursingState(int nursingState)
    {
        this.nursingState = nursingState;
    }

    public String getBreed()
    {
        return breed;
    }

    public void setBreed(String breed)
    {
        this.breed = breed;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getGender()
    {
        return gender;
    }

    public void setGender(int gender)
    {
        this.gender = gender;
    }

    public String getAvatarPath()
    {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath)
    {
        this.avatarPath = avatarPath;
    }

    private int getAgeInMonths()
    {
        /*
        converts the cat's millisecond age to a date to convert it into months
         */
        int ageInMonths;

        long age = getDateOfBirth().getTime();

        long timeDifference = System.currentTimeMillis() - age;

        Date ageAsDate = new Date(timeDifference);
        Calendar ageAsCalendar = Calendar.getInstance();
        ageAsCalendar.setTime(ageAsDate);

        ageInMonths = ageAsCalendar.get(Calendar.MONTH);

        return ageInMonths;
    }

    private double getOptimalFatProteinRatio()
    {
        double fatProteinRatio;

        if (getAgeInMonths() < 12)
            fatProteinRatio = CatNeeds.KITTEN_PROTEIN_FAT_RATIO;
        else
            fatProteinRatio = CatNeeds.ADULT_PROTEIN_FAT_RATIO;

        return fatProteinRatio;
    }

    public int getId()
    {
        return id;
    }

    public class CatNeeds
    {
        // values are in micrograms per kg of cat weight
        public static final double DAILY_VITAMIN_A = 15.43235835;
        public static final double DAILY_VITAMIN_D = 0.097983228;
        public static final double DAILY_VITAMIN_E = 612.3951727;
        public static final double DAILY_VITAMIN_K = 20.08656167;
        public static final double DAILY_VITAMIN_B1 = 80.8361628; // thiamine
        public static final double DAILY_VITAMIN_B2 = 66.13867866; // riboflavin
        public static final double DAILY_VITAMIN_B3 = 612.3951727; // niacin
        public static final double DAILY_VITAMIN_B5 = 97.98322764; // pantothenic acid
        public static final double DAILY_VITAMIN_B12 = 0.342941297;
        public static final double DAILY_VITAMIN_B6 = 39.19329106;
        public static final double DAILY_VITAMIN_B9 = 11.51302925; // folic acid
        public static final double DAILY_CALCIUM = 44092.45244;
        public static final double DAILY_PHOSPHORUS = 39193.29106;
        public static final double DAILY_MAGNESIUM = 6123.951727;
        public static final double DAILY_SODIUM = 10288.2389;
        public static final double DAILY_POTASSIUM = 80836.1628;
        public static final double DAILY_CHLORINE = 14697.48415;
        public static final double DAILY_IRON = 1224.790345;
        public static final double DAILY_COPPER = 73.48742073;
        public static final double DAILY_ZINC = 1126.807118;
        public static final double DAILY_MANGANESE = 73.48742073;
        public static final double DAILY_SELENIUM = 4.654203313;
        public static final double DAILY_IODINE = 21.55631008;

        public static final double ADULT_PROTEIN_FAT_RATIO = 2.272;
        public static final double KITTEN_PROTEIN_FAT_RATIO = 2.5;
        public static final double OPTIMAL_PHOSPHORUS_TO_CALCIUM_RATIO = 1.2;
        public static final double MIN_PHOSPHORUS_TO_CALCIUM_RATIO = 1.1;
        public static final double MAX_PHOSPHORUS_TO_CALCIUM_RATIO = 1.4;

    }
}
