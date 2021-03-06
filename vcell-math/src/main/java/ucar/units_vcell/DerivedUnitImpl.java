package ucar.units_vcell;

/**
 * Provides support for a concrete implementation of derived units.
 * @author Steven R. Emmerson
 * @version $Id: DerivedUnitImpl.java,v 1.4 2000/07/18 20:15:19 steve Exp $
 */
public class
DerivedUnitImpl
    extends	UnitImpl
    implements	DerivedUnit, DerivableUnit
{
    /**
     * The dimensionless derived unit.
     */
    public static final DerivedUnitImpl	DIMENSIONLESS = new DerivedUnitImpl();

    /**
     * The dimension of this derived unit.
     * @serial
     */
    private /*final*/ UnitDimension	dimension;

    /**
     * Constructs a dimensionless derived unit from nothing.
     */
    protected
    DerivedUnitImpl()
    {
	// dimensionless derived unit
	this(new UnitDimension(), dimensionlessID());
    }

    /**
     * Returns the identifiers associated with the dimensionless, derived unit.
     * @return			The identifiers of the dimensionless, derived 
     *				unit.
     */
    private static UnitName
    dimensionlessID()
    {
	UnitName	id;
	try
	{
	    id = UnitName.newUnitName("", "", "");
	}
	catch (NameException e)
	{
	    id = null;
	}
	return id;
    }

    /**
     * Constructs from a unit dimension.  This is a trusted constructor for use
     * by subclasses only.
     * @param dimension		The unit dimension.
     */
    protected
    DerivedUnitImpl(UnitDimension dimension)
    {
	this(dimension, null);
    }

    /**
     * Constructs from identifiers.  This is a trusted constructor for use by
     * subclasses only.
     * @param id		The identifiers for the unit.
     */
    protected
    DerivedUnitImpl(UnitName id)
    {
	this(null, id);
    }

    /**
     * Constructs from a unit dimension and identifiers.  This is a trusted
     * constructor for use by subclasses only.
     * @param dimension		The unit dimension.
     * @param id		The identifiers for the unit.
     */
    protected
    DerivedUnitImpl(UnitDimension dimension, UnitName id)
    {
	super(id);
	this.dimension = dimension;
    }

    /**
     * Sets the unit dimension of this derived unit.  This is a trusted method
     * for use by subclasses only and should be called only once immediately
     * after construction of this superinstance.
     * @param dimension		The unit dimension.
     */
    protected void
    setDimension(UnitDimension dimension)
    {
	this.dimension = dimension;
    }

    /**
     * Returns the unit dimension of this derived unit.
     * @return			The unit dimension of this derived unit.
     */
    public final UnitDimension
    getDimension()
    {
	return dimension;
    }

    /**
     * Returns the quantity dimension of this derived unit.
     * @return                  The quantity dimension of this derived unit.
     */
    public final QuantityDimension
    getQuantityDimension()
    {
	return getDimension().getQuantityDimension();
    }

    /*
     * From DerivedUnit:
     */

    /**
     * Indicates if this derived unit is the reciprocal of another derived
     * unit (e.g. "second" and "hertz").
     * @param that              The other, derived unit.
     */
    public final boolean
    isReciprocalOf(DerivedUnit that)
    {
	return dimension.isReciprocalOf(that.getDimension());
    }

    /*
     * From UnitImpl:
     */

    /**
     * Returns the derived unit that is convertible with this unit.  Obviously,
     * the method returns this derived unit.
     * @return                  <code>this</code>.
     */
    public final DerivedUnit
    getDerivedUnit()
    {
	return this;
    }

    /**
     * Clones the derived unit changing the identifiers.
     * @param id                The identifiers for the new unit.
     * @return                  The new unit.
     */
    public final Unit
    clone(UnitName id)
    {
	return new DerivedUnitImpl(dimension, id);
    }

    /**
     * Multiplies this derived unit by another.
     * @param that              The other unit.
     * @return                  The product of the two units.
     * @throws MultiplyException        Can't multiply these units.
     */
    protected Unit
    myMultiplyBy(Unit that)
	throws MultiplyException
    {
	Unit	result;
	if (dimension.getRank() == 0)
	    result = that;
	else
	{
	    if (!(that instanceof DerivedUnit))
		result = that.multiplyBy(this);
	    else
	    {
		UnitDimension	thatDimension =
		    ((DerivedUnit)that).getDimension();
		result = thatDimension.getRank() == 0
			    ? this
			    : new DerivedUnitImpl(
				dimension.multiplyBy(thatDimension));
	    }
	}
	return result;
    }

    /**
     * Divides this derived unit by another.
     * @param that              The other unit.
     * @return                  The quotient of the two units.
     * @throws OperationException       Can't divide these units.
     */
    protected Unit
    myDivideBy(Unit that)
	throws OperationException
    {
	Unit	result;
	if (dimension.getRank() == 0)
	    result = that.raiseTo(new RationalNumber(-1));
	else
	{
	    if (!(that instanceof DerivedUnit))
		result = that.divideInto(this);
	    else
	    {
		UnitDimension	thatDimension =
		    ((DerivedUnit)that).getDimension();
		result = thatDimension.getRank() == 0
			    ? this
			    : new DerivedUnitImpl(
				dimension.divideBy(thatDimension));
	    }
	}
	return result;
    }

    /**
     * Divides this derived unit into another.
     * @param that              The other unit.
     * @return                  The quotient of the two units.
     * @throws OperationException       Can't divide these units.
     */
    protected Unit
    myDivideInto(Unit that)
	throws OperationException
    {
	return that.divideBy(this);
    }

    /**
     * Converts a numerical value from this unit to the derived unit.
     * Obviously, the numerical value is unchanged.
     * @param amount            The numerical values in this unit.
     * @return                  The numerical value in the derived unit.
     */
    public final float
    toDerivedUnit(float amount)
    {
	return amount;
    }

    /**
     * Converts a numerical value from this unit to the derived unit.
     * Obviously, the numerical value is unchanged.
     * @param amount            The numerical values in this unit.
     * @return                  The numerical value in the derived unit.
     */
    public final double
    toDerivedUnit(double amount)
    {
	return amount;
    }

    /**
     * Converts numerical values from this unit to the derived unit.
     * Obviously, the numerical values are unchanged.
     * @param input             The numerical values in this unit.
     * @param output            The numerical values in the derived unit.  May
     *				be the same array as <code>input</code>.
     * @return                  <code>output</code>.
     */
    public final float[]
    toDerivedUnit(float[] input, float[] output)
    {
	if (input != output)
	    System.arraycopy(input, 0, output, 0, input.length);
	return output;
    }

    /**
     * Converts numerical values from this unit to the derived unit.
     * Obviously, the numerical values are unchanged.
     * @param input             The numerical values in this unit.
     * @param output            The numerical values in the derived unit.  May
     *				be the same array as <code>input</code>.
     * @return                  <code>output</code>.
     */
    public final double[]
    toDerivedUnit(double[] input, double[] output)
    {
	if (input != output)
	    System.arraycopy(input, 0, output, 0, input.length);
	return output;
    }

    /**
     * Converts a numerical value to this unit from the derived unit.
     * Obviously, the numerical value is unchanged.
     * @param amount            The numerical values in the derived unit.
     * @return                  The numerical value in this unit.
     */
    public final float
    fromDerivedUnit(float amount)
    {
	return amount;
    }

    /**
     * Converts a numerical value to this unit from the derived unit.
     * Obviously, the numerical value is unchanged.
     * @param amount            The numerical values in the derived unit.
     * @return                  The numerical value in this unit.
     */
    public final double
    fromDerivedUnit(double amount)
    {
	return amount;
    }

    /**
     * Converts numerical values to this unit from the derived unit.
     * Obviously, the numerical values are unchanged.
     * @param input             The numerical values in the derived unit.
     * @param output            The numerical values in this unit.  May
     *				be the same array as <code>input</code>.
     * @return                  <code>output</code>.
     */
    public final float[]
    fromDerivedUnit(float[] input, float[] output)
    {
	return toDerivedUnit(input, output);
    }

    /**
     * Converts numerical values to this unit from the derived unit.
     * Obviously, the numerical values are unchanged.
     * @param input             The numerical values in the derived unit.
     * @param output            The numerical values in this unit.  May
     *				be the same array as <code>input</code>.
     * @return                  <code>output</code>.
     */
    public final double[]
    fromDerivedUnit(double[] input, double[] output)
    {
	return toDerivedUnit(input, output);
    }

    /**
     * Indicates if values in this unit are convertible with another unit.
     * @param that              The other unit.
     * @return                  <code>true</code> if and only if values in
     *				this unit are convertible to values in <code>
     *				that</code>.
     */
    public final boolean
    isCompatible(Unit that)
    {
	return that instanceof DerivedUnit
		? dimension.equals(((DerivedUnit)that).getDimension()) || 
		    isReciprocalOf((DerivedUnit)that)
		: that.isCompatible(this);
    }

    /**
     * Indicates if this derived unit is semantically identical to an
     * object.
     * @param object		The object
     * @return			<code>true</code> if and only if this derived
     *				unit is semantically identical to <code>
     *				object</code>.
     */
    public boolean
    equals(Object object)
    {
	return 
	    object instanceof DerivedUnit
		? super.equals(object) &&
		    dimension.equals(((DerivedUnit)object).getDimension())
		: object.equals(this);
    }

    /**
     * Indicates if this derived unit is dimensionless.
     * @return			<code>true</code> if and only if this derived
     *				unit is dimensionless.
     */
    public boolean
    isDimensionless()
    {
	return dimension.isDimensionless();
    }

    /**
     * Returns a string representation of this unit.  If the symbol or name
     * is available, then that is returned; otherwise, the corresponding
     * expression in base units is returned.
     * @return			The string expression for this derived unit.
     */
    public String
    toString()
    {
	String	string = super.toString();	// get symbol or name
	return string != null
		? string
		: dimension.toString();
    }

    /**
     * Tests this class.
     */
    public static void
    main(String[] args)
	throws	Exception
    {
	BaseUnit	second = 
	    BaseUnit.getOrCreate(
		UnitName.newUnitName("second", null, "s"), BaseQuantity.TIME);
	System.out.println("second = \"" + second + '"');
	BaseUnit	meter = 
	    BaseUnit.getOrCreate(
		UnitName.newUnitName("meter", null, "m"), BaseQuantity.LENGTH);
	System.out.println("meter = \"" + meter + '"');
	DerivedUnitImpl	meterSecond =
	    (DerivedUnitImpl)meter.myMultiplyBy(second);
	System.out.println("meterSecond = \"" + meterSecond + '"');
	DerivedUnitImpl	meterPerSecond =
	    (DerivedUnitImpl)meter.myDivideBy(second);
	System.out.println("meterPerSecond = \"" + meterPerSecond + '"');
	DerivedUnitImpl	secondPerMeter =
	    (DerivedUnitImpl)second.myDivideBy(meter);
	System.out.println("secondPerMeter = \"" + secondPerMeter + '"');
	System.out.println("meterPerSecond.isReciprocalOf(secondPerMeter)=" +
	    meterPerSecond.isReciprocalOf(secondPerMeter));
	System.out.println("meter.toDerivedUnit(1.0)=" +
	    meter.toDerivedUnit(1.0));
	System.out.println(
	    "meter.toDerivedUnit(new double[] {1,2,3}, new double[3])[1]=" +
	    meter.toDerivedUnit(new double[] {1,2,3}, new double[3])[1]);
	System.out.println("meter.fromDerivedUnit(1.0)=" +
	    meter.fromDerivedUnit(1.0));
	System.out.println(
	    "meter.fromDerivedUnit(new double[] {1,2,3}, new double[3])[2]=" +
	    meter.fromDerivedUnit(new double[] {1,2,3}, new double[3])[2]);
	System.out.println("meter.isCompatible(meter)=" +
	    meter.isCompatible(meter));
	System.out.println("meter.isCompatible(second)=" +
	    meter.isCompatible(second));
	System.out.println("meter.equals(meter)=" + meter.equals(meter));
	System.out.println("meter.equals(second)=" + meter.equals(second));
	System.out.println("meter.isDimensionless()=" + 
	    meter.isDimensionless());
	Unit	sPerS = second.myDivideBy(second);
	System.out.println("sPerS = \"" + sPerS + '"');
	System.out.println("sPerS.isDimensionless()=" + 
	    sPerS.isDimensionless());
	Unit	squaredMeterPerSecond = meterPerSecond.raiseTo(new RationalNumber(2));
	Unit	s = meter.myDivideBy((DerivedUnitImpl)meterPerSecond);
    }

    /**
     * Raises this derived unit to a power.
     * @param power             The power.
     * @return                  This derived unit raised to the given power.
     */
    protected Unit
    myRaiseTo(RationalNumber power)
    {
	return power.doubleValue() == 1.0
		? this
		: new DerivedUnitImpl(dimension.raiseTo(power));
    }
}
