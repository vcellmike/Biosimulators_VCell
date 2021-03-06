/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package cbit.vcell.client.pyvcellproxy;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)")
public class PlotData implements org.apache.thrift.TBase<PlotData, PlotData._Fields>, java.io.Serializable, Cloneable, Comparable<PlotData> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("PlotData");

  private static final org.apache.thrift.protocol.TField TIME_POINTS_FIELD_DESC = new org.apache.thrift.protocol.TField("timePoints", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField DATA_FIELD_DESC = new org.apache.thrift.protocol.TField("data", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new PlotDataStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new PlotDataTupleSchemeFactory();

  public java.util.List<java.lang.Double> timePoints; // required
  public java.util.List<java.lang.Double> data; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TIME_POINTS((short)1, "timePoints"),
    DATA((short)2, "data");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // TIME_POINTS
          return TIME_POINTS;
        case 2: // DATA
          return DATA;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TIME_POINTS, new org.apache.thrift.meta_data.FieldMetaData("timePoints", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.LIST        , "TimePoints")));
    tmpMap.put(_Fields.DATA, new org.apache.thrift.meta_data.FieldMetaData("data", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.LIST        , "Data")));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(PlotData.class, metaDataMap);
  }

  public PlotData() {
  }

  public PlotData(
    java.util.List<java.lang.Double> timePoints,
    java.util.List<java.lang.Double> data)
  {
    this();
    this.timePoints = timePoints;
    this.data = data;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public PlotData(PlotData other) {
    if (other.isSetTimePoints()) {
      java.util.List<java.lang.Double> __this__timePoints = new java.util.ArrayList<java.lang.Double>(other.timePoints.size());
      for (java.lang.Double other_element : other.timePoints) {
        __this__timePoints.add(other_element);
      }
      this.timePoints = __this__timePoints;
    }
    if (other.isSetData()) {
      java.util.List<java.lang.Double> __this__data = new java.util.ArrayList<java.lang.Double>(other.data.size());
      for (java.lang.Double other_element : other.data) {
        __this__data.add(other_element);
      }
      this.data = __this__data;
    }
  }

  public PlotData deepCopy() {
    return new PlotData(this);
  }

  @Override
  public void clear() {
    this.timePoints = null;
    this.data = null;
  }

  public int getTimePointsSize() {
    return (this.timePoints == null) ? 0 : this.timePoints.size();
  }

  public java.util.Iterator<java.lang.Double> getTimePointsIterator() {
    return (this.timePoints == null) ? null : this.timePoints.iterator();
  }

  public void addToTimePoints(double elem) {
    if (this.timePoints == null) {
      this.timePoints = new java.util.ArrayList<java.lang.Double>();
    }
    this.timePoints.add(elem);
  }

  public java.util.List<java.lang.Double> getTimePoints() {
    return this.timePoints;
  }

  public PlotData setTimePoints(java.util.List<java.lang.Double> timePoints) {
    this.timePoints = timePoints;
    return this;
  }

  public void unsetTimePoints() {
    this.timePoints = null;
  }

  /** Returns true if field timePoints is set (has been assigned a value) and false otherwise */
  public boolean isSetTimePoints() {
    return this.timePoints != null;
  }

  public void setTimePointsIsSet(boolean value) {
    if (!value) {
      this.timePoints = null;
    }
  }

  public int getDataSize() {
    return (this.data == null) ? 0 : this.data.size();
  }

  public java.util.Iterator<java.lang.Double> getDataIterator() {
    return (this.data == null) ? null : this.data.iterator();
  }

  public void addToData(double elem) {
    if (this.data == null) {
      this.data = new java.util.ArrayList<java.lang.Double>();
    }
    this.data.add(elem);
  }

  public java.util.List<java.lang.Double> getData() {
    return this.data;
  }

  public PlotData setData(java.util.List<java.lang.Double> data) {
    this.data = data;
    return this;
  }

  public void unsetData() {
    this.data = null;
  }

  /** Returns true if field data is set (has been assigned a value) and false otherwise */
  public boolean isSetData() {
    return this.data != null;
  }

  public void setDataIsSet(boolean value) {
    if (!value) {
      this.data = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case TIME_POINTS:
      if (value == null) {
        unsetTimePoints();
      } else {
        setTimePoints((java.util.List<java.lang.Double>)value);
      }
      break;

    case DATA:
      if (value == null) {
        unsetData();
      } else {
        setData((java.util.List<java.lang.Double>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case TIME_POINTS:
      return getTimePoints();

    case DATA:
      return getData();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case TIME_POINTS:
      return isSetTimePoints();
    case DATA:
      return isSetData();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof PlotData)
      return this.equals((PlotData)that);
    return false;
  }

  public boolean equals(PlotData that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_timePoints = true && this.isSetTimePoints();
    boolean that_present_timePoints = true && that.isSetTimePoints();
    if (this_present_timePoints || that_present_timePoints) {
      if (!(this_present_timePoints && that_present_timePoints))
        return false;
      if (!this.timePoints.equals(that.timePoints))
        return false;
    }

    boolean this_present_data = true && this.isSetData();
    boolean that_present_data = true && that.isSetData();
    if (this_present_data || that_present_data) {
      if (!(this_present_data && that_present_data))
        return false;
      if (!this.data.equals(that.data))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetTimePoints()) ? 131071 : 524287);
    if (isSetTimePoints())
      hashCode = hashCode * 8191 + timePoints.hashCode();

    hashCode = hashCode * 8191 + ((isSetData()) ? 131071 : 524287);
    if (isSetData())
      hashCode = hashCode * 8191 + data.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(PlotData other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetTimePoints()).compareTo(other.isSetTimePoints());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTimePoints()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.timePoints, other.timePoints);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetData()).compareTo(other.isSetData());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetData()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.data, other.data);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("PlotData(");
    boolean first = true;

    sb.append("timePoints:");
    if (this.timePoints == null) {
      sb.append("null");
    } else {
      sb.append(this.timePoints);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("data:");
    if (this.data == null) {
      sb.append("null");
    } else {
      sb.append(this.data);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (timePoints == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'timePoints' was not present! Struct: " + toString());
    }
    if (data == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'data' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class PlotDataStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public PlotDataStandardScheme getScheme() {
      return new PlotDataStandardScheme();
    }
  }

  private static class PlotDataStandardScheme extends org.apache.thrift.scheme.StandardScheme<PlotData> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, PlotData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TIME_POINTS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list16 = iprot.readListBegin();
                struct.timePoints = new java.util.ArrayList<java.lang.Double>(_list16.size);
                double _elem17;
                for (int _i18 = 0; _i18 < _list16.size; ++_i18)
                {
                  _elem17 = iprot.readDouble();
                  struct.timePoints.add(_elem17);
                }
                iprot.readListEnd();
              }
              struct.setTimePointsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // DATA
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list19 = iprot.readListBegin();
                struct.data = new java.util.ArrayList<java.lang.Double>(_list19.size);
                double _elem20;
                for (int _i21 = 0; _i21 < _list19.size; ++_i21)
                {
                  _elem20 = iprot.readDouble();
                  struct.data.add(_elem20);
                }
                iprot.readListEnd();
              }
              struct.setDataIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, PlotData struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.timePoints != null) {
        oprot.writeFieldBegin(TIME_POINTS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.DOUBLE, struct.timePoints.size()));
          for (double _iter22 : struct.timePoints)
          {
            oprot.writeDouble(_iter22);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.data != null) {
        oprot.writeFieldBegin(DATA_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.DOUBLE, struct.data.size()));
          for (double _iter23 : struct.data)
          {
            oprot.writeDouble(_iter23);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class PlotDataTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public PlotDataTupleScheme getScheme() {
      return new PlotDataTupleScheme();
    }
  }

  private static class PlotDataTupleScheme extends org.apache.thrift.scheme.TupleScheme<PlotData> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, PlotData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        oprot.writeI32(struct.timePoints.size());
        for (double _iter24 : struct.timePoints)
        {
          oprot.writeDouble(_iter24);
        }
      }
      {
        oprot.writeI32(struct.data.size());
        for (double _iter25 : struct.data)
        {
          oprot.writeDouble(_iter25);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, PlotData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TList _list26 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.DOUBLE, iprot.readI32());
        struct.timePoints = new java.util.ArrayList<java.lang.Double>(_list26.size);
        double _elem27;
        for (int _i28 = 0; _i28 < _list26.size; ++_i28)
        {
          _elem27 = iprot.readDouble();
          struct.timePoints.add(_elem27);
        }
      }
      struct.setTimePointsIsSet(true);
      {
        org.apache.thrift.protocol.TList _list29 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.DOUBLE, iprot.readI32());
        struct.data = new java.util.ArrayList<java.lang.Double>(_list29.size);
        double _elem30;
        for (int _i31 = 0; _i31 < _list29.size; ++_i31)
        {
          _elem30 = iprot.readDouble();
          struct.data.add(_elem30);
        }
      }
      struct.setDataIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

