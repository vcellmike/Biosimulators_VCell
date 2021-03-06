package ucar.units_vcell;


/*
 * Provides support for the concept of the dimension of a unit.  The
 * dimension of a unit is like the dimension of a quantity except that
 * the the individual quantity dimensions have been replaced with the
 * corresponding base unit.  For example, the dimension of the quantity
 * "force" is "M.L.t-2" and the dimension of the corresponding SI unit
 * (the "newton") is "kg.m.s-2".
 *
 * Instances of this class are immutable.
 *
 * @author Steven R. Emmerson
 * @version $Id: UnitDimension.java,v 1.4 2000/07/18 20:15:32 steve Exp $
 */
public final class
UnitDimension
    extends	Dimension
{
    /**
     * Constructs a dimensionless unit dimension.
     */
    public
    UnitDimension()
    {
	super();
    }


    /**
     * Constructs a unit dimension comprised of the given factors.
     * @param factors		The factors that constitute the unit dimension.
     */
    private
    UnitDimension(Factor[] factors)
    {
	super(factors);
    }


    /**
     * Constructs the unit dimension corresponding to a base unit.
     * @param baseUnit		A base unit.
     */
    public
    UnitDimension(BaseUnit baseUnit)
    {
	super(new Factor(baseUnit));
    }


    /**
     * Divides this unit dimension by another.
     * @param that		The other unit dimension.
     * @return			The quotient of this unit dimension divided
     *				by the other.
     */
    public UnitDimension
    divideBy(UnitDimension that)
    {
	return multiplyBy(that.raiseTo(new RationalNumber(-1)));
    }


    /**
     * Returns the corresponding quantity dimension.
     * @return			The quantity dimension corresponding to this
     *				unit dimension.
     */
    public QuantityDimension
    getQuantityDimension()
    {
	Factor[]	factors = getFactors();
	for (int i = factors.length; --i >= 0; )
	{
	    Factor	factor = factors[i];
	    factors[i] =
		new Factor(
		    ((BaseUnit)factor.getBase()).getBaseQuantity(), 
		    factor.getExponent());
	}
	return new QuantityDimension(factors);
    }


    /**
     * Tests this class.
     */
    public static void
    main(String[] args)
	throws	Exception
    {
	System.out.println("new UnitDimension() = \"" +
	    new UnitDimension() + '"');
	UnitDimension	timeDimension =
	    new UnitDimension(
		BaseUnit.getOrCreate(
		    UnitName.newUnitName("second", null, "s"),
		    BaseQuantity.TIME));
	System.out.println("timeDimension = \"" + timeDimension + '"');
	UnitDimension	lengthDimension =
	    new UnitDimension(
		BaseUnit.getOrCreate(
		    UnitName.newUnitName("meter", null, "m"),
		    BaseQuantity.LENGTH));
	System.out.println("lengthDimension = \"" + lengthDimension + '"');
	System.out.println(
	    "lengthDimension.isReciprocalOf(timeDimension) = \"" +
	    lengthDimension.isReciprocalOf(timeDimension) + '"');
	UnitDimension	hertzDimension = timeDimension.raiseTo(new RationalNumber(-1));
	System.out.println("hertzDimension = \"" + hertzDimension + '"');
	System.out.println(
	    "hertzDimension.isReciprocalOf(timeDimension) = \"" +
	    hertzDimension.isReciprocalOf(timeDimension) + '"');
	System.out.println("lengthDimension.divideBy(timeDimension) = \"" +
	    lengthDimension.divideBy(timeDimension) + '"');
	System.out.println(
	    "lengthDimension.divideBy(timeDimension).raiseTo(2) = \"" +
	    lengthDimension.divideBy(timeDimension).raiseTo(new RationalNumber(2)) + '"');
    }


    /**
     * Multiplies this unit dimension by another.
     * @param that		The other unit dimension.
     * @return			The product of this unit dimension multiplied
     *				by the other.
     */
    public UnitDimension
    multiplyBy(UnitDimension that)
    {
	return new UnitDimension(mult(that));
    }


    /**
     * Raises this unit dimension to a power.
     * @param power		The power.
     * @return			The result of raising this unit dimension
     *				to the power.
     */
    public UnitDimension
    raiseTo(RationalNumber power)
    {
	return new UnitDimension(pow(power));
    }
}