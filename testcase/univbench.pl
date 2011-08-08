:- auto_table.
subConj("fresh1", "Person", "Employee").
subClass("University", "Organization").
range("publicationResearch", "Research").
range("publicationAuthor", "Person").
subClass("FullProfessor", "Professor").
subConj("fresh2", "Person", "Chair").
domain("subOrganizationOf", "Organization").
subEx("headOf", "College", "fresh6").
subClass("ResearchGroup", "Organization").
subConj("fresh6", "Person", "Dean").
subClass("AssistantProfessor", "Professor").
domain("hasAlumnus", "University").
subClass("Book", "Publication").
subClass("Faculty", "Employee").
subClass("Lecturer", "Faculty").
subEx("worksFor", "Organization", "fresh1").
range("listedCourse", "Course").
subRole("headOf", "worksFor").
range("orgPublication", "Publication").
subClass("JournalArticle", "Article").
domain("member", "Organization").
range("degreeFrom", "University").
range("doctoralDegreeFrom", "University").
subClass("SystemsStaff", "AdministrativeStaff").
subClass("ResearchAssistant", "Student").
subClass("PostDoc", "Faculty").
subClass("Chair", "Professor").
subClass("Research", "Work").
domain("mastersDegreeFrom", "Person").
domain("affiliatedOrganizationOf", "Organization").
supEx("TeachingAssistant", "teachingAssistantOf", "Course", "e1").
subRole("undergraduateDegreeFrom", "degreeFrom").
subClass("Student", "Person").
subConj("fresh5", "Person", "Director").
subClass("VisitingProfessor", "Professor").
supEx("Director", "headOf", "Program", "e2").
subClass("TechnicalReport", "Article").
subClass("GraduateCourse", "Course").
supEx("Student", "takesCourse", "Course", "e3").
domain("researchProject", "ResearchGroup").
subClass("Chair", "Person").
subClass("ClericalStaff", "AdministrativeStaff").
subClass("GraduateStudent", "Person").
subConj("fresh4", "Person", "Student").
subClass("UndergraduateStudent", "Student").
domain("advisor", "Person").
subClass("TeachingAssistant", "Person").
range("hasAlumnus", "Person").
supEx("ResearchAssistant", "worksFor", "ResearchGroup", "e4").
subClass("Professor", "Faculty").
range("mastersDegreeFrom", "University").
subClass("Manual", "Publication").
subEx("teachingAssistantOf", "Course", "fresh3").
domain("publicationAuthor", "Publication").
subClass("Dean", "Person").
subRole("doctoralDegreeFrom", "degreeFrom").
subClass("Dean", "Professor").
range("undergraduateDegreeFrom", "University").
domain("softwareVersion", "Software").
subConj("fresh3", "Person", "TeachingAssistant").
subClass("Employee", "Person").
subClass("Program", "Organization").
subClass("AssociateProfessor", "Professor").
range("affiliatedOrganizationOf", "Organization").
range("subOrganizationOf", "Organization").
range("member", "Person").
subRole("worksFor", "memberOf").
range("softwareDocumentation", "Publication").
subEx("headOf", "Program", "fresh5").
subClass("AdministrativeStaff", "Employee").
supEx("Dean", "headOf", "College", "e5").
range("affiliateOf", "Person").
range("advisor", "Professor").
subRole("mastersDegreeFrom", "degreeFrom").
domain("doctoralDegreeFrom", "Person").
subClass("Course", "Work").
supEx("Chair", "headOf", "Department", "e6").
subClass("Director", "Person").
subEx("takesCourse", "Course", "fresh4").
domain("tenured", "Professor").
range("researchProject", "Research").
domain("degreeFrom", "Person").
subClass("UnofficialPublication", "Publication").
domain("orgPublication", "Organization").
subClass("Specification", "Publication").
domain("undergraduateDegreeFrom", "Person").
subClass("ConferencePaper", "Article").
subClass("Software", "Publication").
subClass("Article", "Publication").
subClass("Department", "Organization").
domain("teacherOf", "Faculty").
domain("teachingAssistantOf", "TeachingAssistant").
subEx("headOf", "Department", "fresh2").
range("teachingAssistantOf", "Course").
subClass("College", "Organization").
domain("publicationDate", "Publication").
domain("affiliateOf", "Organization").
supEx("Employee", "worksFor", "Organization", "e7").
domain("listedCourse", "Schedule").
subClass("Institute", "Organization").
domain("softwareDocumentation", "Software").
domain("publicationResearch", "Publication").
range("teacherOf", "Course").
supEx("GraduateStudent", "takesCourse", "GraduateCourse", "e8").
rol("publicationAuthor").
rol("orgPublication").
rol("undergraduateDegreeFrom").
rol("mastersDegreeFrom").
rol("researchProject").
rol("member").
rol("tenured").
rol("subOrganizationOf").
rol("affiliateOf").
rol("softwareDocumentation").
rol("advisor").
rol("memberOf").
rol("listedCourse").
rol("publicationResearch").
rol("teacherOf").
rol("takesCourse").
rol("publicationDate").
rol("teachingAssistantOf").
rol("hasAlumnus").
rol("degreeFrom").
rol("doctoralDegreeFrom").
rol("worksFor").
rol("headOf").
rol("softwareVersion").
rol("affiliatedOrganizationOf").
cls("Book").
cls("Student").
cls("Dean").
cls("Department").
cls("Manual").
cls("Article").
cls("FullProfessor").
cls("UnofficialPublication").
cls("TeachingAssistant").
cls("Organization").
cls("SystemsStaff").
cls("UndergraduateStudent").
cls("Faculty").
cls("College").
cls("Chair").
cls("VisitingProfessor").
cls("AdministrativeStaff").
cls("Course").
cls("fresh6").
cls("Specification").
cls("fresh5").
cls("fresh4").
cls("ResearchAssistant").
cls("fresh3").
cls("fresh2").
cls("fresh1").
cls("Employee").
cls("Director").
cls("ResearchGroup").
cls("Program").
cls("Publication").
cls("AssociateProfessor").
cls("Work").
cls("ConferencePaper").
cls("ClericalStaff").
cls("GraduateStudent").
cls("Lecturer").
cls("Institute").
cls("Research").
cls("PostDoc").
cls("GraduateCourse").
cls("AssistantProfessor").
cls("Software").
cls("University").
cls("Person").
cls("Professor").
cls("TechnicalReport").
cls("Schedule").
cls("JournalArticle").
inst(X, X) :- nom(X).
self(X, V) :- nom(X), rol(V), triple(X, V, X).
inst(X, Z) :- subClass(Y, Z), inst(X, Y).
inst(X, Z) :- subConj(Y1, Y2, Z), inst(X, Y1), inst(X, Y2).
inst(X, Z) :- rol(Y), rol(V), subEx(V, Y, Z), triple(X, V, X1), inst(X1, Y).
inst(X, Z) :- rol(Y), subEx(V, Y, Z), self(X, V), inst(X, Y).
triple(X, V, X1) :- supEx(Y, V, Z, X1), inst(X, Y).
inst(X1, Z) :- rol(V), supEx(Y, V, Z, X1), inst(X, Y).
inst(X, Z) :- rol(V), subSelf(V, Z), self(X, V).
self(X, V) :- rol(V), supSelf(Y, V), inst(X, Y).
triple(X, W, X1) :- rol(V), rol(W), subRole(V, W), triple(X, V, X1).
self(X, W) :- rol(V), rol(W), subRole(V, W), self(X, V).
triple(X, W, X11) :- rol(U), rol(V), rol(W), subRChain(U, V, W), triple(X, U, X1), triple(X1, V, X11).
triple(X, W, X1) :- rol(U), rol(V), rol(W), subRChain(U, V, W), self(X, U), triple(X, V, X1).
triple(X, W, X1) :- rol(U), rol(V), rol(W), subRChain(U, V, W), triple(X, U, X1), self(X1, V).
triple(X, W, X) :- rol(U), rol(V), rol(W), subRChain(U, V, W), self(X, U), self(X, V).
triple(X, W, X1) :- rol(V1), rol(V2), rol(W), subRConj(V1, V2, W), triple(X, V1, X1), triple(X, V2, X1).
self(X, W) :- rol(V1), rol(V2), rol(W), subRConj(V1, V2, W), self(X, V1), self(X, V2).
inst(Y, Z) :- inst(X, Y), nom(Y), inst(X, Z).
inst(X, Z) :- inst(X, Y), nom(Y), inst(Y, Z).
triple(Z, U, Y) :- inst(X, Y), nom(Y), triple(Z, U, X).
triple(X, V, X) :- nom(X), rol(V), self(X, V).
inst(Y, R) :- range(V, R), triple(X, V, Y).
inst(X, R) :- domain(V, R), triple(X, V, Y).
top("Thing").
bot("Nothing").
subConj(X, Y, Z) :- fail.
subEx(X, Y, Z) :- fail.
supEx(X, Y, Z, W) :- fail.
subSelf(X, Y) :- fail.
supSelf(X, Y) :- fail.
self(X, Y) :- fail.
subProd(X, Y, Z) :- fail.
supProd(X, Y, Z) :- fail.
subRConj(X, Y, Z) :- fail.
subRChain(X, Y, Z) :- fail.
subClass(X, Y) :- fail.
subRole(X, Y) :- fail.
inst(X, Y) :- fail.
triple(X, Y, Z) :- fail.
nom(X) :- fail.
rol(X) :- fail.
cls(X) :- fail.
range(X, Y) :- fail.
domain(X, Y) :- fail.
domainD(X, Y) :- fail.
