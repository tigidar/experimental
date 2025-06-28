package exp.ext


//TODO: make these into macros, so that we don't use runtime cpu on them
extension (o: Option[String])
  def ?~ = o.getOrElse("")
  def ?:(default: String) = default

extension (i: Int)
  inline def px = s"${i.toString()}px"
  inline def %% = s"${i.toString()}%"

